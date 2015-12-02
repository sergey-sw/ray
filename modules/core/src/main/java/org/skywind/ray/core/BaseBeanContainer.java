package org.skywind.ray.core;

import org.skywind.ray.log.ContextLogger;
import org.skywind.ray.meta.InterfaceAudience;
import org.skywind.ray.util.Exceptions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Author: Sergey42
 * Date: 17.05.2015 19:23
 */
@InterfaceAudience.Development
@SuppressWarnings("unchecked")
public class BaseBeanContainer implements InternalBeanContainer {

    protected final Map<String, BeanDefinition> definitionByName = new ConcurrentHashMap<>();
    protected final Map<Class, BeanDefinition> definitionByClass = new ConcurrentHashMap<>();
    protected final Map<Class, Optional<BeanDefinition>> resolvedDefinitionsByClass = new ConcurrentHashMap<>();

    protected final ContextLogger logger;
    protected final BeanLifecycleProcessor beanLifecycleProcessor;

    public BaseBeanContainer(ContextLogger logger) {
        this.logger = logger;
        this.beanLifecycleProcessor = new BaseBeanLifecycleProcessor(this, logger);
    }

    @Override
    public boolean containsBean(String name) {
        return definitionByName.containsKey(Objects.requireNonNull(name));
    }

    @Override
    public boolean containsBean(Class clazz) {
        return definitionByClass.containsKey(Objects.requireNonNull(clazz));
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        beanClass = Objects.requireNonNull(beanClass);
        BeanDefinition beanDefinition = checkNotNull(getBeanDefinition(beanClass), beanClass);
        validateScope(beanDefinition, Scope.SINGLETON);
        return (T) beanDefinition.singletonInstance;
    }

    @Override
    public <T> T getBean(String name) {
        name = Objects.requireNonNull(name);
        BeanDefinition beanDefinition = checkNotNull(definitionByName.get(name), name);
        validateScope(beanDefinition, Scope.SINGLETON);
        return (T) beanDefinition.singletonInstance;
    }

    @Override
    public <T> Collection<T> getBeansByType(final Class<T> beanClass) {
        return getDefinitionsOfType(beanClass).stream()
                .filter(d -> d.scope == Scope.SINGLETON)
                .map(d -> (T) d.singletonInstance)
                .collect(Collectors.toList());
    }

    @Override
    public <T> T createPrototype(Class<T> beanClass) {
        beanClass = Objects.requireNonNull(beanClass);
        BeanDefinition beanDefinition = getBeanDefinition(beanClass);
        validateScope(beanDefinition, Scope.PROTOTYPE);
        return createPrototype(beanDefinition);
    }

    @Override
    public <T> T createPrototype(String name) {
        name = Objects.requireNonNull(name);
        BeanDefinition beanDefinition = checkNotNull(definitionByName.get(name), name);
        validateScope(beanDefinition, Scope.PROTOTYPE);
        return createPrototype(beanDefinition);
    }

    @Override
    public <T> T createPrototype(Class<T> beanClass, Object... constructorParams) {
        beanClass = Objects.requireNonNull(beanClass);
        BeanDefinition beanDefinition = checkNotNull(getBeanDefinition(beanClass), beanClass);
        validateScope(beanDefinition, Scope.PROTOTYPE);

        try {
            T prototypeInstance = (T) beanDefinition.managedConstructor.newInstance(constructorParams);
            beanLifecycleProcessor.autowireFields(prototypeInstance, beanDefinition);
            beanLifecycleProcessor.invokeInitMethods(prototypeInstance, beanDefinition);

            logger.debug("Created prototype instance of class " + beanClass.getSimpleName());

            return prototypeInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.error(Exceptions.toStr("Failed to create a prototype of class: " + beanClass.getName(), e));
            throw new BeanInstantiationException(e);
        }
    }

    @Override
    public <T> T createPrototype(String name, Object... constructorParams) {
        name = Objects.requireNonNull(name);
        BeanDefinition beanDefinition = checkNotNull(definitionByName.get(name), name);
        validateScope(beanDefinition, Scope.PROTOTYPE);

        try {
            T prototypeInstance = (T) beanDefinition.managedConstructor.newInstance(constructorParams);
            beanLifecycleProcessor.autowireFields(prototypeInstance, beanDefinition);
            beanLifecycleProcessor.invokeInitMethods(prototypeInstance, beanDefinition);

            logger.debug("Created prototype instance with name " + name);

            return prototypeInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.error(Exceptions.toStr("Failed to create a prototype with name: " + name, e));
            throw new BeanInstantiationException(e);
        }
    }

    @Override
    public void register(BeanDefinition beanDefinition) {
        Objects.requireNonNull(beanDefinition);
        definitionByName.put(beanDefinition.id, beanDefinition);
        definitionByClass.put(beanDefinition.beanClass, beanDefinition);

        logger.info(String.format("Registered bean of class %s with scope %s",
                beanDefinition.beanClass.getName(), beanDefinition.scope.getId()));
    }

    @Override
    public void autowireSingletons() {
        StreamSupport.stream(getBeanDefinitions().spliterator(), false)
                .filter(d -> d.scope == Scope.SINGLETON)
                .forEach(d -> beanLifecycleProcessor.autowireFields(d.singletonInstance, d));
    }

    @Override
    public void initSingletons() {
        StreamSupport.stream(getBeanDefinitions().spliterator(), false)
                .filter(d -> d.scope == Scope.SINGLETON)
                .forEach(d -> beanLifecycleProcessor.invokeInitMethods(d.singletonInstance, d));
    }

    @Override
    public synchronized void destroyBeans() {
        logger.info("Destroying beans in container : " + this);
        definitionByClass.values().forEach(d -> beanLifecycleProcessor.invokeDestroyMethods(d.singletonInstance, d));
        definitionByClass.clear();
        definitionByName.clear();
    }

    @Override
    public int size() {
        return definitionByClass.size();
    }

    protected Iterable<BeanDefinition> getBeanDefinitions() {
        return definitionByClass.values();
    }

    @Override
    public <T> T getBeanAnyScope(Class<T> beanClass) {
        beanClass = Objects.requireNonNull(beanClass);
        BeanDefinition beanDefinition = checkNotNull(definitionByClass.get(beanClass), beanClass);

        if (Scope.SINGLETON == beanDefinition.scope) {
            return (T) beanDefinition.singletonInstance;
        } else {
            return createPrototype(beanClass);
        }
    }

    protected <T> T createPrototype(BeanDefinition beanDefinition) {
        beanDefinition = Objects.requireNonNull(beanDefinition);
        try {
            T prototypeInstance = (T) beanDefinition.beanClass.newInstance();
            beanLifecycleProcessor.autowireFields(prototypeInstance, beanDefinition);
            beanLifecycleProcessor.invokeInitMethods(prototypeInstance, beanDefinition);
            return prototypeInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    protected Collection<BeanDefinition> getDefinitionsOfType(final Class beanClass) {
        Objects.requireNonNull(beanClass);
        return definitionByClass.values().stream()
                .filter(d -> beanClass.isAssignableFrom(d.beanClass))
                .collect(Collectors.toList());
    }

    @Override
    public BeanDefinition getBeanDefinition(Class clazz) {
        BeanDefinition definition = definitionByClass.get(clazz);
        if (definition != null) {
            return definition;
        }

        Optional<BeanDefinition> optional = resolvedDefinitionsByClass.get(clazz);
        if (optional != null) {
            return optional.orElse(null);
        }

        Collection<BeanDefinition> definitionsOfType = getDefinitionsOfType(clazz);
        if (definitionsOfType.isEmpty()) {
            resolvedDefinitionsByClass.put(clazz, Optional.empty());
            return null;
        }

        if (definitionsOfType.size() > 1) {
            String msg = BeanInstantiationException.getMultipleCandidatesMessage(clazz, definitionsOfType);
            logger.error(msg);
            throw new BeanInstantiationException(msg);
        }

        definition = definitionsOfType.iterator().next();
        resolvedDefinitionsByClass.put(clazz, Optional.of(definition));
        return definition;
    }

    protected BeanDefinition checkNotNull(BeanDefinition definition, String id) {
        if (definition == null) {
            logger.error("Bean not found: " + id);
            throw new BeanNotFoundException(id);
        }
        return definition;
    }

    protected BeanDefinition checkNotNull(BeanDefinition definition, Class id) {
        if (definition == null) {
            logger.error("Bean not found : " + id.getName());
            throw new BeanNotFoundException(id);
        }
        return definition;
    }

    protected void validateScope(BeanDefinition definition, Scope expected) {
        if (definition.scope != expected) {
            String msg = BeanInstantiationException.getScopeValidationMessage(definition, expected);
            logger.error(msg);
            throw new BeanInstantiationException(msg);
        }
    }
}
