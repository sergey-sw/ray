package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.LifecycleBean;
import org.skywind.ray.core.Context;


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
