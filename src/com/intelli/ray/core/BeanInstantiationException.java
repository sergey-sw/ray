package com.intelli.ray.core;

/**
 * Author: Sergey42
 * Date: 16.11.13 20:27
 */
public class BeanInstantiationException extends RuntimeException {

    static final String SCOPE_PATTERN = "Failed to instantiate bean with name %s. Expected scope: %s. Actual scope: %s.";

    public BeanInstantiationException(String message) {
        super(message);
    }

    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanInstantiationException(Throwable cause) {
        super(cause);
    }

    public BeanInstantiationException(BeanDefinition definition, Scope expected) {
        super(String.format(SCOPE_PATTERN, definition, expected, definition.getScope()));
    }
}
