package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Exception indicated, that instantiation of managed component failed for some reason.
 * This can happen for many reasons, for example because of inaccessible class constructors or
 * wrong scope declarations.
 * <p/>
 * Author: Sergey42
 * Date: 16.11.13 20:27
 */
@InterfaceAudience.Public
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

    public static String getScopeValidationMessage(BeanDefinition definition, Scope expected) {
        return String.format(SCOPE_PATTERN, definition, expected, definition.getScope());
    }
}
