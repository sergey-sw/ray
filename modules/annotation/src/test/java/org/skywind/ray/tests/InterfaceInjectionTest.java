package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.MultiInterface;
import org.skywind.ray.base_scope.MultiInterfaceImpl1;
import org.skywind.ray.base_scope.MultiInterfaceImpl2;
import org.skywind.ray.base_scope.MyInterface;
import org.skywind.ray.core.AnnotationContext;
import org.skywind.ray.core.BeanInstantiationException;
import org.skywind.ray.core.BeanNotFoundException;
import org.skywind.ray.core.Context;
import org.skywind.ray.resolving_scope.BeanX;

import java.util.Collection;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 07.11.2015 19:49
 */
public class InterfaceInjectionTest extends TestCase {

    public void test() throws Exception {
        Context context = new AnnotationContext("org.skywind.ray.base_scope");
        context.refresh();

        MyInterface bean = context.getBeanContainer().getBean(MyInterface.class);
        assertNotNull(bean);
    }

    public void test2() throws Exception {
        Context context = new AnnotationContext("org.skywind.ray.base_scope");
        context.refresh();

        context.getBeanContainer().getBean(MultiInterfaceImpl1.class);
        context.getBeanContainer().getBean(MultiInterfaceImpl2.class);

        try {
            context.getBeanContainer().getBean(MultiInterface.class);
        } catch (BeanInstantiationException e) {
            return;
        }
        fail("Should not resolve MultiInterface because of 2 implementations");
    }

    public void test3() throws Exception {
        Context context = new AnnotationContext("org.skywind.ray.base_scope");
        context.refresh();

        try {
            context.getBeanContainer().getBean(NotABean.class);
        } catch (BeanNotFoundException e) {
            return;
        }
        fail("Should not resolve NotABean because of no implementations");
    }

    public void test4() throws Exception {
        Context context = new AnnotationContext("org.skywind.ray.base_scope");
        context.refresh();

        Collection<NotABean> beansByType = context.getBeanContainer().getBeansByType(NotABean.class);
        assertEquals(0, beansByType.size());
    }

    public void test5() throws Exception {
        Context context = new AnnotationContext("org.skywind.ray.resolving_scope");
        context.refresh();

        BeanX beanX = context.getBeanContainer().getBean(BeanX.class);
        assertNotNull(beanX.getMyInt());
    }

    static class NotABean {
    }
}
