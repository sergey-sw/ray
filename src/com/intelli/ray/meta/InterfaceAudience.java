package com.intelli.ray.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotations to inform users of the API intended audience.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 23:40
 */
@InterfaceAudience.Public
public class InterfaceAudience {

    /**
     * Intended for usage by library end users.
     * References to the stable public part of the API
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Public {
    }

    /**
     * Intended for usage by developers, who want to extend basic
     * functionality by providing customized implementations.
     * Components with this annotation should not be used by library clients.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Development {
    }

    /**
     * Components marked with this annotation belong to library
     * internals and should not be used outside the library.
     * They also can be changed or removed in any time.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Private {
    }
}
