package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Exception indicates that requested component is not found in application context.
 * This may be caused by incorrect component declaration or wrong context dependencies.
 * <p/>
 * Author: Sergey42
 * Date: 18.05.14 16:11
 */
@InterfaceAudience.Public
public class BeanNotFoundException extends IllegalArgumentException {

    String msg;
    static String classPattern = "Bean of class '%s' is not present in context";
    static String namePattern = "Bean with name '%s' is not present in context";

    public BeanNotFoundException(Class id) {
        msg = String.format(classPattern, id.getName());
    }

    public BeanNotFoundException(String id) {
        msg = String.format(namePattern, id);
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
