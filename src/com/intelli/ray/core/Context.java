package com.intelli.ray.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Author: Sergey42
 * Date: 21.12.2014 20:58
 */
public interface Context {

    boolean isStarted();

    <T> T getBean(Class<T> beanClass);

    <T> T getBean(String name);

    <T> Collection<T> getBeansByType(Class<T> beanClass);

    <T> T getPrototype(String name);

    <T> T getManagedConstructorBean(Class<T> beanClass, Object... params);

    void printConfiguredBeans(OutputStream outputStream) throws IOException;
}
