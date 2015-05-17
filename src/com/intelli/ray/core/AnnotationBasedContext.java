package com.intelli.ray.core;

import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:11
 */
public class AnnotationBasedContext implements Context {

    protected String[] scanLocations;
    protected BeanContainer beanContainer = new SimpleBeanContainer();

    protected volatile boolean started = false;

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
    public BeanContainer getBeanContainer() {
        return beanContainer;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void start() throws ContextInitializationException {
        registerBeans();
        injectSingletonDependencies();
        invokePostConstruct();
        started = true;
    }

    protected void registerBeans() {
        Reflections reflections = new Reflections(scanLocations);

        for (Class clazz : reflections.getTypesAnnotatedWith(ManagedComponent.class)) {
            try {
                ManagedComponent ann = (ManagedComponent) clazz.getAnnotation(ManagedComponent.class);
                String beanId = !ann.name().isEmpty() ? ann.name() : clazz.getName();

                Scope scope = ann.scope();
                Constructor managedConstructor = ReflectionHelper.getManagedConstructor(clazz);
                if (scope == Scope.SINGLETON && managedConstructor != null) {
                    throw new ContextInitializationException("Managed constructor is not supported in singletons");
                }

                BeanDefinition definition;
                if (managedConstructor != null) {
                    definition = new BeanDefinition(beanId, scope, clazz, null, managedConstructor);
                } else {
                    definition = new BeanDefinition(beanId, scope, clazz, clazz.newInstance(), null);
                }

                beanContainer.register(definition);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ContextInitializationException(e);
            }
        }
    }

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

    @SuppressWarnings("unchecked")
    protected void doInject(Field field, Object beanInstance, BeanDefinition definition) {
        Class fieldClazz = field.getType();
        BeanDefinition fieldBeanDefinition = beanContainer.getBeanDefinition(fieldClazz);
        if (fieldBeanDefinition == null) {
            throw new ContextInitializationException("Can not inject property '" + field.getName() + "' in bean " +
                    definition.beanClass.getName() + ", because property bean class " + fieldClazz.getName()
                    + " is not present in context.");
        }
        try {
            if (!field.isAccessible()) field.setAccessible(true);
            field.set(beanInstance, beanContainer.getBeanAnyScope(fieldBeanDefinition.beanClass));
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    protected void invokePostConstruct() {
        for (BeanDefinition beanDefinition : beanContainer.getBeanDefinitions()) {
            if (beanDefinition.scope == Scope.SINGLETON) {
                try {
                    invokePostConstructMethods(beanDefinition.singletonInstance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new BeanInstantiationException("Failed to execute post construct methods of bean " +
                            beanDefinition.beanClass.getName(), e);
                }
            }
        }
    }

    protected void invokePostConstructMethods(Object component) throws InvocationTargetException, IllegalAccessException {
        List<Method> postConstructMethods = new ArrayList<>(4);
        List<String> methodNames = new ArrayList<>(4);
        Class clazz = component.getClass();
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

        ListIterator<Method> iterator = postConstructMethods.listIterator(postConstructMethods.size());
        while (iterator.hasPrevious()) {
            Method method = iterator.previous();
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(component);
        }
    }
}
