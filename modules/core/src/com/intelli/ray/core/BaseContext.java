package com.intelli.ray.core;

import com.intelli.ray.log.ContextLogger;
import com.intelli.ray.log.LoggerRegistry;
import com.intelli.ray.meta.InterfaceAudience;

import java.util.*;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 22:55
 */
@InterfaceAudience.Development
public abstract class BaseContext implements Context {

    protected final ContextLogger logger = new ContextLogger();
    protected volatile InternalBeanContainer beanContainer;
    protected ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    protected Set<String> profiles = new HashSet<>();

    protected volatile boolean started = false;
    protected boolean closed = false;
    protected long refreshStartTs;

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
            started = false;

            beforeRefresh();
            createBeanContainer();
            try {
                registerBeanDefinitions();
                beanContainer.autowireSingletons();
                beanContainer.initSingletons();
                afterRefresh();

                started = true;
            } catch (BeanInstantiationException e) {
                logger.error("Caught exception during context start. Begin destruction...");
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

    @Override
    public void setClassLoader(ClassLoader contextClassLoader) {
        this.contextClassLoader = contextClassLoader;
    }

    @Override
    public void setProfiles(Collection<String> activeProfiles) {
        if (isActive()) {
            logger.warn("Profiles are set while context is active. Changes will not be applied until next refresh.");
        }

        Objects.requireNonNull(activeProfiles);
        this.profiles = new HashSet<>();

        for (String profile : activeProfiles) {
            this.profiles.add(Objects.requireNonNull(profile));
        }
    }

    @Override
    public Collection<String> getProfiles() {
        return Collections.unmodifiableCollection(profiles);
    }

    protected boolean acceptProfiles(Collection<String> componentProfiles) {
        if (componentProfiles == null || componentProfiles.isEmpty()) {
            return profiles.isEmpty();
        }
        if (profiles.isEmpty()) {
            return false;
        }

        for (String componentProfile : componentProfiles) {
            if (this.profiles.contains(componentProfile)) {
                return true;
            }
        }
        return false;
    }

    protected InternalBeanContainer createBeanContainer() {
        if (beanContainer != null) {
            beanContainer.destroyBeans();
        }
        beanContainer = new BaseBeanContainer(logger);
        return beanContainer;
    }

    protected void beforeRefresh() {
        refreshStartTs = System.currentTimeMillis();
    }

    protected void afterRefresh() {
        logContextInfo();
    }

    protected void logContextInfo() {
        logger.info("Context profiles: " + Arrays.deepToString(getProfiles().toArray()));
        logger.info("Total registered beans number: " + beanContainer.size());
        logger.info("Refresh duration: " + (System.currentTimeMillis() - refreshStartTs) + " ms");
    }
}
