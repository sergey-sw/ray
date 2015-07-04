package com.intelli.ray.core;

import com.intelli.ray.log.ContextLogger;
import com.intelli.ray.log.LoggerRegistry;
import com.intelli.ray.meta.InterfaceAudience;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 22:55
 */
@InterfaceAudience.Development
public abstract class BaseContext implements Context {

    protected final ContextLogger logger = new ContextLogger();
    protected volatile InternalBeanContainer beanContainer;

    protected volatile boolean started = false;
    protected boolean closed = false;

    protected final Object lifecycleMonitor = new Object();

    protected abstract void registerBeanDefinitions();

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
                beanContainer.autowireSingletons();
                beanContainer.initSingletons();

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
        beanContainer = new BaseBeanContainer(logger);
        return beanContainer;
    }
}
