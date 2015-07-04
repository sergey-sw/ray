package com.intelli.ray.core;

import com.intelli.ray.log.LoggerRegistry;
import com.intelli.ray.meta.InterfaceAudience;

/**
 * Context represents a scope of related application components.
 * <p/>
 * Author: Sergey42
 * Date: 21.12.2014 20:58
 */
@InterfaceAudience.Public
public interface Context {

    /**
     * Checks if context is fully initialized
     *
     * @return TRUE, if initialization phase was passed successfully
     */
    boolean isActive();

    /**
     * Refreshes the context, destroys all created managed components,
     * re-reads configuration and reloads bean definitions.
     */
    void refresh();

    /**
     * Destroys the context with all created managed components.
     */
    void destroy();

    /**
     * Returns the instance of BeanContainer, associated with the current context.
     * See {@link com.intelli.ray.core.BeanContainer}
     */
    BeanContainer getBeanContainer();

    /**
     * Returns the instance of LoggerRegistry, associated with the current context.
     * See {@link com.intelli.ray.log.LoggerRegistry}
     */
    LoggerRegistry getLoggerRegistry();
}
