package org.skywind.ray.core;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 17.07.2015 17:49
 */
public class ContextStartupException extends RuntimeException {

    public ContextStartupException(Throwable cause) {
        super(cause);
    }

    public ContextStartupException(String message) {
        super(message);
    }

    public ContextStartupException(String message, Throwable cause) {
        super(message, cause);
    }
}
