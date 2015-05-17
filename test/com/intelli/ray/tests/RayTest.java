package com.intelli.ray.tests;

import com.intelli.ray.core.AnnotationBasedContext;
import com.intelli.ray.core.Context;
import junit.framework.TestCase;

/**
 * Author: Sergey42
 * Date: 17.05.2015 21:27
 */
public abstract class RayTest extends TestCase {

    public static Context context;

    static {
        context = new AnnotationBasedContext("com.intelli.ray.base_scope");
        context.start();
    }
}
