package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.Bean3;
import org.skywind.ray.core.BeanContainer;
import org.skywind.ray.core.Context;
import org.skywind.ray.core.XmlContext;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 14:20
 */
public class AllFieldsTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:org/skywind/ray/context.xml");
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
