package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Author: Sergey42
 * Date: 17.05.2015 19:24
 */
@InterfaceAudience.Development
public class BeanDefinition {

    protected final String id;
    protected final Scope scope;
    protected final Class beanClass;
    protected final Object singletonInstance;
    protected final Constructor managedConstructor;

    protected final Method[] initMethods;
    protected final Method[] destroyMethods;
    protected final Field[] autowiredFields;

    public BeanDefinition(String id, Scope scope, Class beanClass, Constructor constructor,
                          Method[] initMethods, Method[] destroyMethods, Field[] autowiredFields) {
        this.id = Objects.requireNonNull(id);
        this.scope = Objects.requireNonNull(scope);
        this.beanClass = Objects.requireNonNull(beanClass);
        this.destroyMethods = Objects.requireNonNull(destroyMethods);
        this.autowiredFields = Objects.requireNonNull(autowiredFields);
        this.initMethods = Objects.requireNonNull(initMethods);
        this.managedConstructor = Objects.requireNonNull(constructor);

        this.singletonInstance = null;
    }

    public BeanDefinition(String id, Scope scope, Class beanClass, Object singletonInstance,
                          Method[] initMethods, Method[] destroyMethods, Field[] autowiredFields) {
        this.id = Objects.requireNonNull(id);
        this.scope = Objects.requireNonNull(scope);
        this.beanClass = Objects.requireNonNull(beanClass);
        this.destroyMethods = Objects.requireNonNull(destroyMethods);
        this.autowiredFields = Objects.requireNonNull(autowiredFields);
        this.initMethods = Objects.requireNonNull(initMethods);
        this.singletonInstance = Objects.requireNonNull(singletonInstance);

        this.managedConstructor = null;
    }

    public String getId() {
        return id;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", scope=" + scope +
                ", beanClass=" + beanClass.getName() +
                '}';
    }
}
