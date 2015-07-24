package com.intelli.ray.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Profile represents a scope of components, that is used in a specific environment.
 * Profiles can be used to split production and test environment components.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 12.07.2015 16:47
 */
@InterfaceAudience.Public
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Profile {

    String[] value();
}
