package com.intelli.ray.core;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:18
 */
public class ContextInitializationException extends RuntimeException {

    public ContextInitializationException() {
    }

    public ContextInitializationException(String message) {
        super(message);
    }

    public ContextInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextInitializationException(Throwable cause) {
        super(cause);
    }

    public ContextInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
