package com.intelli.ray.core;

/**
 * Exception indicated that requested bean is not found in bean context.
 * This may be caused by missing annotation or incorrect context dependencies.
 * <p/>
 * Author: Sergey42
 * Date: 18.05.14 16:11
 */
public class BeanNotFoundException extends IllegalArgumentException {

    public <T> BeanNotFoundException(Class<T> beanClass) {
        super(String.format("Bean of class '%s' is not present in context", beanClass.getName()));
    }

    public BeanNotFoundException(String name) {
        super(String.format("Bean with name '%s' is not present in context", name));
    }
}
