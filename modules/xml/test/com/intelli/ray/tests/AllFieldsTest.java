package com.intelli.ray.tests;

import com.intelli.ray.Bean3;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import com.intelli.ray.core.XmlContext;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 14:20
 */
public class AllFieldsTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:com/intelli/ray/context.xml");
        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();

        Bean3 bean3 = beanContainer.getBean(Bean3.class);

        assertEquals(2, bean3.getInit());
        assertNotNull(bean3.getBean1());
        assertNotNull(bean3.getBean2());

        context.destroy();

        assertEquals(2, bean3.getDestroy());
    }
}
