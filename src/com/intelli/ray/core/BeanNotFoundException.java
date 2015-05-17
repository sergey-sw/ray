package com.intelli.ray.core;

/**
 * Exception indicated that requested bean is not found in bean context.
 * This may be caused by missing annotation or incorrect context dependencies.
 * <p/>
 * Author: Sergey42
 * Date: 18.05.14 16:11
 */
public class BeanNotFoundException extends IllegalArgumentException {

    Object id;
    static String classPattern = "Bean of class '%s' is not present in context";
    static String namePattern = "Bean with name '%s' is not present in context";


    public BeanNotFoundException(Object id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        if (id instanceof String) {
            return String.format(namePattern, id);
        } else if (id instanceof Class) {
            return String.format(classPattern, ((Class) id).getName());
        } else {
            return super.getMessage();
        }
    }
}
