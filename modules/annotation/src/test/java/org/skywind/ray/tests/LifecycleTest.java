package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.subPack.DisposableBean;
import org.skywind.ray.core.AnnotationContext;
import org.skywind.ray.core.Context;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 23:44
 */
public class LifecycleTest extends TestCase {

    public void test() throws Exception {
        Context context = new AnnotationContext("org.skywind.ray.base_scope");

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
