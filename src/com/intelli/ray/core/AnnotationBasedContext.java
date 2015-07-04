package com.intelli.ray.core;

import com.intelli.ray.reflection.Scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
            Method[] initMethods = getUniqueMethodsAnnotatedWith(clazz, getConfiguration().getInitMethodAnnotations());
            Method[] destroyMethods = getUniqueMethodsAnnotatedWith(clazz, getConfiguration().getDestroyMethodAnnotations());
            Field[] autowiredFields = getFieldsAnnotatedWith(clazz, getConfiguration().getAutowiredAnnotations());
            if (constructor != null) {
                definition = new BeanDefinition(beanId, scope, clazz, constructor, initMethods, destroyMethods, autowiredFields);
            } else {
                try {
                    definition = new BeanDefinition(beanId, scope, clazz, clazz.newInstance(), initMethods, destroyMethods, autowiredFields);
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
            Field[] fields = getFieldsAnnotatedWith(beanInstance.getClass(), getConfiguration().getAutowiredAnnotations());

            for (Field field : fields) {
                doInject(field, beanInstance, definition);
            }
        }
    }
}
