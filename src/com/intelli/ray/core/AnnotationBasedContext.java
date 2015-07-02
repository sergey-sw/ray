package com.intelli.ray.core;

import com.intelli.ray.reflection.ReflectionHelper;
import com.intelli.ray.reflection.Scanner;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:11
 */
public class AnnotationBasedContext extends BaseContext {

    protected String[] scanLocations;

    /**
     * Builds the context by annotation scanning in specified locations.
     * Location order provides overriding.
     *
     * @param scanLocations - package paths
     */
    public AnnotationBasedContext(String... scanLocations) {
        this.scanLocations = scanLocations;
    }

    @Override
    protected void registerBeanDefinitions() {
        Scanner scanner = new Scanner(logger, scanLocations);

        for (Class clazz : ReflectionHelper.getTypesAnnotatedWith(scanner.getClasses(), ManagedComponent.class)) {

            ManagedComponent ann = (ManagedComponent) clazz.getAnnotation(ManagedComponent.class);
            String beanId = !ann.name().isEmpty() ? ann.name() : clazz.getName();

            Scope scope = ann.scope();
            Constructor managedConstructor = ReflectionHelper.getManagedConstructor(clazz);
            if (scope == Scope.SINGLETON && managedConstructor != null) {
                throw new BeanInstantiationException("Managed constructor is not supported in singletons");
            }

            BeanDefinition definition;
            Method[] initMethods = extractInitMethods(clazz);
            if (managedConstructor != null) {
                definition = new BeanDefinition(beanId, scope, clazz, managedConstructor, initMethods);
            } else {
                try {
                    definition = new BeanDefinition(beanId, scope, clazz, clazz.newInstance(), initMethods);
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
            Field[] fields = ReflectionHelper.getAllAnnotatedFields(beanInstance.getClass(), Inject.class);

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
                if (method.isAnnotationPresent(PostConstruct.class) && !methodNames.contains(method.getName())) {
                    postConstructMethods.add(method);
                    methodNames.add(method.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }

        int size = postConstructMethods.size();
        return postConstructMethods.toArray(new Method[size]);
    }
}
