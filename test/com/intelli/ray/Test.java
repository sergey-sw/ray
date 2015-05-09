package com.intelli.ray;

import com.intelli.ray.core.AnnotationBasedContext;
import com.intelli.ray.core.Context;

/**
 * Author: Sergey42
 * Date: 16.11.13 17:06
 */
public class Test {

    public static void main(String[] args) {
        // Context context = new Context(Test.class);
        Context context = new AnnotationBasedContext("com.intelli.ray");
    }
}
