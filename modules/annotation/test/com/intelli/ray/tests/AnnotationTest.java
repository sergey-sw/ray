package com.intelli.ray.tests;

import com.intelli.ray.core.AnnotationContext;
import com.intelli.ray.core.Context;
import junit.framework.TestCase;

/**
 * Author: Sergey42
 * Date: 17.05.2015 21:27
 */
public abstract class AnnotationTest extends TestCase {

    public static Context context;

    static {
        context = new AnnotationContext("com.intelli.ray.base_scope");
        context.refresh();
    }
}
