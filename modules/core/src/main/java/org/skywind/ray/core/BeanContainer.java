package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

import java.util.Collection;

/**
 * BeanContainer provides access to registered beans.
 * <p/>
 * Author: Sergey42
 * Date: 17.05.2015 17:24
 */
@InterfaceAudience.Public
public interface BeanContainer {

    boolean containsBean(String name);

    boolean containsBean(Class clazz);

    <T> T getBean(Class<T> beanClass) throws BeanNotFoundException;

    <T> T getBean(String name) throws BeanNotFoundException;

    <T> Collection<T> getBeansByType(Class<T> beanClass);

    <T> T createPrototype(Class<T> beanClass) throws BeanNotFoundException;

    <T> T createPrototype(String name) throws BeanNotFoundException;

    <T> T createPrototype(Class<T> beanClass, Object... constructorParams) throws BeanNotFoundException;

    <T> T createPrototype(String name, Object... constructorParams) throws BeanNotFoundException;

    int size();
}
