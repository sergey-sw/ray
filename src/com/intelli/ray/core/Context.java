package com.intelli.ray.core;

import com.intelli.ray.log.LoggerRegistry;

/**
 * Context represents a scope of related application components.
 * <p/>
 * Author: Sergey42
 * Date: 21.12.2014 20:58
 */
public interface Context {

    boolean isStarted();

    void start();

    BeanContainer getBeanContainer();

    LoggerRegistry getLoggerRegistry();
}
