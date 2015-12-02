package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

import java.util.Arrays;
import java.util.Collection;

/**
 * Exception indicates that instantiation of managed component failed.
 * This can happen because of inaccessible class constructors/wrong scope declarations/multiple autowiring candidates.
 * <p/>
 * Author: Sergey42
 * Date: 16.11.13 20:27
 */
@InterfaceAudience.Public
public class BeanInstantiationException extends RuntimeException {

    static final String SCOPE_PATTERN = "Failed to instantiate bean with name %s. Expected scope: %s. Actual scope: %s.";
    static final String MULTIPLE_CANDIDATE_PATTERN = "Failed to instantiate bean for class %s. Multiple candidates were " +
            "found: %s";

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

    public static String getMultipleCandidatesMessage(Class clazz, Collection<BeanDefinition> candidates) {
        return String.format(MULTIPLE_CANDIDATE_PATTERN, clazz, Arrays.toString(candidates.toArray()));
    }
}
