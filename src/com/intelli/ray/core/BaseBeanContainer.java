package com.intelli.ray.core;

import com.intelli.ray.log.ContextLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Sergey42
 * Date: 17.05.2015 19:23
 */
@SuppressWarnings("unchecked")
public class BaseBeanContainer implements InternalBeanContainer {

    protected final Map<String, BeanDefinition> definitionByName = new ConcurrentHashMap<>();
    protected final Map<Class, BeanDefinition> definitionByClass = new ConcurrentHashMap<>();
    protected final ContextLogger logger;
    protected final BeanLifecycleProcessor beanLifecycleProcessor;

    public BaseBeanContainer(ContextLogger logger) {
        this.logger = logger;
        this.beanLifecycleProcessor = new BaseBeanLifecycleProcessor(this, logger);
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        BeanDefinition beanDefinition = checkNotNull(definitionByClass.get(beanClass), beanClass);
        beanDefinition.scopeShouldBe(Scope.SINGLETON);
        return (T) beanDefinition.singletonInstance;
    }

    @Override
    public <T> T getBean(String name) {
        BeanDefinition beanDefinition = checkNotNull(definitionByName.get(name), name);
        beanDefinition.scopeShouldBe(Scope.SINGLETON);
        return (T) beanDefinition.singletonInstance;
    }

    @Override
    public <T> Collection<T> getBeansByType(Class<T> beanClass) {
        List<T> beansOfType = new ArrayList<>();
        for (BeanDefinition definition : definitionByClass.values()) {
            if (definition.scope == Scope.SINGLETON && beanClass.isAssignableFrom(definition.beanClass)) {
                beansOfType.add((T) definition.singletonInstance);
            }
        }
        return beansOfType;
    }

    @Override
    public <T> T createPrototype(Class<T> beanClass) {
        BeanDefinition beanDefinition = checkNotNull(definitionByClass.get(beanClass), beanClass);
        beanDefinition.scopeShouldBe(Scope.PROTOTYPE);
        return createPrototype(beanDefinition);
    }

    @Override
    public <T> T createPrototype(String name) {
        BeanDefinition beanDefinition = checkNotNull(definitionByName.get(name), name);
        beanDefinition.scopeShouldBe(Scope.PROTOTYPE);
        return createPrototype(beanDefinition);
    }

    @Override
    public <T> T createPrototype(Class<T> beanClass, Object... constructorParams) {
        BeanDefinition beanDefinition = checkNotNull(definitionByClass.get(beanClass), beanClass);
        beanDefinition.scopeShouldBe(Scope.PROTOTYPE);

        try {
            //noinspection unchecked
            T prototypeInstance = (T) beanDefinition.managedConstructor.newInstance(constructorParams);
            beanLifecycleProcessor.autowireFields(prototypeInstance, beanDefinition);
            beanLifecycleProcessor.invokeInitMethods(prototypeInstance, beanDefinition);

            logger.log(new Date() + " - Created prototype instance of class " + beanClass.getSimpleName());

            return prototypeInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    @Override
    public <T> T createPrototype(String name, Object... constructorParams) {
        BeanDefinition beanDefinition = checkNotNull(definitionByName.get(name), name);
        beanDefinition.scopeShouldBe(Scope.PROTOTYPE);

        try {
            //noinspection unchecked
            T prototypeInstance = (T) beanDefinition.managedConstructor.newInstance(constructorParams);
            beanLifecycleProcessor.autowireFields(prototypeInstance, beanDefinition);
            beanLifecycleProcessor.invokeInitMethods(prototypeInstance, beanDefinition);

            logger.log(new Date() + " - Created prototype instance with name " + name);

            return prototypeInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(Class beanClass) {
        return definitionByClass.get(beanClass);
    }

    @Override
    public void register(BeanDefinition beanDefinition) {
        definitionByName.put(beanDefinition.id, beanDefinition);
        definitionByClass.put(beanDefinition.beanClass, beanDefinition);

        logger.log(String.format(new Date() + " - Registered bean of class %s with scope %s",
                beanDefinition.beanClass.getName(), beanDefinition.scope.getId()));
    }

    @Override
    public void autowireSingletons() {
        for (BeanDefinition definition : getBeanDefinitions()) {
            if (definition.scope == Scope.SINGLETON) {
                Object beanInstance = definition.singletonInstance;
                beanLifecycleProcessor.autowireFields(beanInstance, definition);
            }
        }
    }

    @Override
    public void initSingletons() {
        for (BeanDefinition beanDefinition : getBeanDefinitions()) {
            if (beanDefinition.scope == Scope.SINGLETON) {
                beanLifecycleProcessor.invokeInitMethods(beanDefinition.singletonInstance, beanDefinition);
            }
        }
    }

    @Override
    public synchronized void destroyBeans() {
        logger.log("Destroying beans in container : " + this);
        for (BeanDefinition definition : definitionByClass.values()) {
            beanLifecycleProcessor.invokeDestroyMethods(definition.singletonInstance, definition);
        }

        definitionByClass.clear();
        definitionByName.clear();
    }

    @Override
    public Iterable<Class> getManagedComponentClasses() {
        return definitionByClass.keySet();
    }

    protected Iterable<BeanDefinition> getBeanDefinitions() {
        return definitionByClass.values();
    }

    @Override
    public <T> T getBeanAnyScope(Class<T> beanClass) {
        BeanDefinition beanDefinition = checkNotNull(definitionByClass.get(beanClass), beanClass);

        if (Scope.SINGLETON == beanDefinition.scope) {
            return (T) beanDefinition.singletonInstance;
        } else {
            return createPrototype(beanClass);
        }
    }

    protected <T> T createPrototype(BeanDefinition beanDefinition) {
        try {
            //noinspection unchecked
            T prototypeInstance = (T) beanDefinition.beanClass.newInstance();
            beanLifecycleProcessor.autowireFields(prototypeInstance, beanDefinition);
            beanLifecycleProcessor.invokeInitMethods(prototypeInstance, beanDefinition);
            return prototypeInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    protected BeanDefinition checkNotNull(BeanDefinition definition, String id) {
        if (definition == null) {
            throw new BeanNotFoundException(id);
        }
        return definition;
    }

    protected BeanDefinition checkNotNull(BeanDefinition definition, Class id) {
        if (definition == null) {
            throw new BeanNotFoundException(id);
        }
        return definition;
    }
}
