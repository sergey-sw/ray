package com.intelli.ray.core;

import com.intelli.ray.log.ContextLogger;
import com.intelli.ray.log.LoggerRegistry;

import java.lang.reflect.Field;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 22:55
 */
public abstract class BaseContext implements Context {

    protected final ContextLogger logger = new ContextLogger();
    protected volatile InternalBeanContainer beanContainer;

    protected volatile boolean started = false;
    protected boolean closed = false;

    protected final Object lifecycleMonitor = new Object();

    protected abstract void registerBeanDefinitions();

    protected abstract void injectSingletonDependencies();

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
                invokeInitMethods();

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

    protected InternalBeanContainer createBeanContainer() {
        if (beanContainer != null) {
            beanContainer.destroyBeans();
        }
        beanContainer = new SimpleBeanContainer(logger);
        return beanContainer;
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

    protected void invokeInitMethods() {
        for (BeanDefinition beanDefinition : beanContainer.getBeanDefinitions()) {
            if (beanDefinition.scope == Scope.SINGLETON) {
                beanContainer.invokeInitMethods(beanDefinition);
            }
        }
    }
}
