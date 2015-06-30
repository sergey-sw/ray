package com.intelli.ray.core;

import com.intelli.ray.log.ContextLogger;
import com.intelli.ray.log.LoggerRegistry;
import com.intelli.ray.reflection.ReflectionHelper;
import com.intelli.ray.reflection.Scanner;

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

    protected final ContextLogger logger = new ContextLogger();
    protected volatile BeanContainer beanContainer;

    protected volatile boolean started = false;
    protected boolean closed = false;

    private final Object lifecycleMonitor = new Object();

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
        if (beanContainer == null) {
            throw new IllegalStateException("Bean container was not created yet or context was destroyed");
        }
        return beanContainer;
    }

    @Override
    public LoggerRegistry getLoggerRegistry() {
        return logger.getLoggerRegistry();
    }

    @Override
    public boolean isActive() {
        return started;
    }

    @Override
    public void refresh() {
        synchronized (lifecycleMonitor) {
            createBeanContainer();
            try {
                registerBeanDefinitions();
                injectSingletonDependencies();
                invokePostConstruct();

                started = true;
            } catch (BeanInstantiationException e) {
                beanContainer.destroyBeans();
                throw e;
            }
        }
    }

    @Override
    public void destroy() {
        synchronized (lifecycleMonitor) {
            boolean doClose = started && !closed;

            if (doClose) {
                beanContainer.destroyBeans();
                beanContainer = null;
                started = false;
                closed = true;
            }
        }
    }

    protected BeanContainer createBeanContainer() {
        if (beanContainer != null) {
            beanContainer.destroyBeans();
        }
        beanContainer = new SimpleBeanContainer(logger);
        return beanContainer;
    }

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
            if (managedConstructor != null) {
                definition = new BeanDefinition(beanId, scope, clazz, managedConstructor);
            } else {
                try {
                    definition = new BeanDefinition(beanId, scope, clazz, clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new BeanInstantiationException(e);
                }
            }

            beanContainer.register(definition);
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
            throw new BeanInstantiationException("Can not inject property '" + field.getName() + "' in bean " +
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
