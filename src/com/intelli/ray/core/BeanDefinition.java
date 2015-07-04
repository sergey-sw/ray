package com.intelli.ray.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Author: Sergey42
 * Date: 17.05.2015 19:24
 */
public class BeanDefinition {

    protected final String id;
    protected final Scope scope;
    protected final Class beanClass;
    protected final Object singletonInstance;
    protected final Constructor managedConstructor;

    protected final Method[] initMethods;
    protected final Field[] autowiredFields;

    public BeanDefinition(String id, Scope scope, Class beanClass, Constructor constructor, Method[] methods, Field[] autowiredFields) {
        this.id = id;
        this.scope = scope;
        this.beanClass = beanClass;
        this.autowiredFields = autowiredFields;
        this.singletonInstance = null;
        this.managedConstructor = constructor;
        this.initMethods = methods;
    }

    public BeanDefinition(String id, Scope scope, Class beanClass, Object singletonInstance, Method[] methods, Field[] autowiredFields) {
        this.id = id;
        this.scope = scope;
        this.beanClass = beanClass;
        this.singletonInstance = singletonInstance;
        this.autowiredFields = autowiredFields;
        this.managedConstructor = null;
        this.initMethods = methods;
    }

    public void scopeShouldBe(Scope expected) {
        if (scope != expected) {
            throw new BeanInstantiationException(this, expected);
        }
    }

    public String getId() {
        return id;
    }

    public Scope getScope() {
        return scope;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    //todo
    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", scope=" + scope +
                ", beanClass=" + beanClass +
                ", singletonInstance=" + singletonInstance +
                ", managedConstructor=" + managedConstructor +
                ", initMethods=" + Arrays.toString(initMethods) +
                ", autowiredFields=" + Arrays.toString(autowiredFields) +
                '}';
    }
}
