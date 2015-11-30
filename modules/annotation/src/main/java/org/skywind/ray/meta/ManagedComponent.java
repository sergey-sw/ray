package org.skywind.ray.meta;

import org.skywind.ray.core.Scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:09
 */
@InterfaceAudience.Public
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagedComponent {

    String name() default "";

    Scope scope() default Scope.SINGLETON;
}
