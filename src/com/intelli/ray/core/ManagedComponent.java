package com.intelli.ray.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:09
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagedComponent {
    String name();

    Scope scope() default Scope.SINGLETON;
}
