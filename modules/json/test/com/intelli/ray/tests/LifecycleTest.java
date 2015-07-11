package com.intelli.ray.tests;

import com.intelli.ray.base_scope.LifecycleBean;
import com.intelli.ray.core.Context;
import junit.framework.TestCase;


/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 16:37
 */
public class LifecycleTest extends TestCase {

    public void test() throws Exception {
        Context context = new JsonTestContext();

        context.refresh();

        LifecycleBean disposableBean = context.getBeanContainer().getBean(LifecycleBean.class);
        assertNotNull(disposableBean);

        assertEquals((Boolean) false, disposableBean.getResourcesReleased());

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
