package com.intelli.ray.tests;

import com.intelli.ray.base_scope.subPack.DisposableBean;
import com.intelli.ray.core.AnnotationBasedContext;
import com.intelli.ray.core.Context;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 23:44
 */
public class LifecycleTest extends TestCase {

    public void test() throws Exception {
        Context context = new AnnotationBasedContext("com.intelli.ray.base_scope");

        context.refresh();

        DisposableBean disposableBean = context.getBeanContainer().getBean(DisposableBean.class);
        assertFalse(disposableBean.getResourcesReleased());

        context.destroy();

        assertTrue(disposableBean.getResourcesReleased());

        try {
            context.getBeanContainer();
        } catch (IllegalStateException e) {
            //ok
            return;
        }
        fail();
    }
}
