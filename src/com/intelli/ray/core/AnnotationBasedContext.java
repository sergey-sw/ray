package com.intelli.ray.core;

import com.intelli.ray.reflection.Scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.intelli.ray.reflection.ReflectionHelper.*;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:11
 */
public class AnnotationBasedContext extends BaseConfigurableContext {

    protected String[] scanLocations;

    /**
     * Builds the context by annotation scanning in specified locations.
     * Location order provides overriding.
     *
     * @param scanLocations - package paths
     */
    public AnnotationBasedContext(String... scanLocations) {
        this(new DefaultAnnotationConfiguration(), scanLocations);
    }

    public AnnotationBasedContext(Configuration configuration, String... scanLocations) {
        super(configuration);
        this.scanLocations = scanLocations;
    }

    @Override
    protected void registerBeanDefinitions() {
        Scanner scanner = new Scanner(logger, scanLocations);
        Configuration configuration = getConfiguration();
        Iterable<Class<? extends Annotation>> annotations = configuration.getManagedComponentAnnotations();
        Map<Class, Class<? extends Annotation>> classMap = getTypesAnnotatedWith(scanner.getClasses(), annotations);
        Configuration.NameAndScopeExtractor nameAndScopeExtractor = getConfiguration().getNameAndScopeExtractor();

        for (Map.Entry<Class, Class<? extends Annotation>> entry : classMap.entrySet()) {
            Class clazz = entry.getKey();
            Class<? extends Annotation> annotationClass = entry.getValue();

            Annotation ann = clazz.getAnnotation(annotationClass);
            Configuration.NameAndScope nameAndScope = nameAndScopeExtractor.extract(ann);

            String beanId = !nameAndScope.name.isEmpty() ? nameAndScope.name : clazz.getName();
            Scope scope = nameAndScope.scope;

            Constructor constructor = getConstructorsAnnotatedWith(clazz, configuration.getManagedConstructorAnnotations());
            if (scope == Scope.SINGLETON && constructor != null) {
                throw new BeanInstantiationException(String.format(
                        "Failed to register %s : Managed constructor is not supported in singletons", clazz));
            }

            BeanDefinition definition;
            Method[] initMethods = extractInitMethods(clazz);
            Field[] autowiredFields = extractAutowiredFields(clazz);
            if (constructor != null) {
                definition = new BeanDefinition(beanId, scope, clazz, constructor, initMethods, autowiredFields);
            } else {
                try {
                    definition = new BeanDefinition(beanId, scope, clazz, clazz.newInstance(), initMethods, autowiredFields);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new BeanInstantiationException(e);
                }
            }

            beanContainer.register(definition);
        }
    }

    @Override
    protected void injectSingletonDependencies() {
        for (BeanDefinition definition : beanContainer.getBeanDefinitions()) {
            if (definition.scope == Scope.PROTOTYPE) continue;

            Object beanInstance = definition.singletonInstance;
            Field[] fields = getAllAnnotatedFields(beanInstance.getClass(), getConfiguration().getAutowiredAnnotations());

            for (Field field : fields) {
                doInject(field, beanInstance, definition);
            }
        }
    }

    protected Method[] extractInitMethods(Class clazz) {
        List<Method> postConstructMethods = new ArrayList<>(4);
        List<String> methodNames = new ArrayList<>(4);

        while (clazz != Object.class) {
            Method[] classMethods = clazz.getDeclaredMethods();
            for (Method method : classMethods) {
                for (Class<? extends Annotation> initAnnotation : getConfiguration().getInitMethodAnnotations()) {
                    if (method.isAnnotationPresent(initAnnotation) && !methodNames.contains(method.getName())) {
                        postConstructMethods.add(method);
                        methodNames.add(method.getName());
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        int size = postConstructMethods.size();
        return postConstructMethods.toArray(new Method[size]);
    }

    protected Field[] extractAutowiredFields(Class clazz) {
        List<Field> autowiredFields = new ArrayList<>();

        while (clazz != Object.class) {
            Field[] classFields = clazz.getDeclaredFields();
            for (Field field : classFields) {
                for (Class<? extends Annotation> autowire : getConfiguration().getAutowiredAnnotations()) {
                    if (field.isAnnotationPresent(autowire)) {
                        autowiredFields.add(field);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        int size = autowiredFields.size();
        return autowiredFields.toArray(new Field[size]);
    }
}
