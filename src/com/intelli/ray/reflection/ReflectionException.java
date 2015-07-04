package com.intelli.ray.reflection;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 22:01
 */
@InterfaceAudience.Private
public class ReflectionException extends RuntimeException {

    public ReflectionException(String s, Exception e) {
        super(s, e);
    }
}
