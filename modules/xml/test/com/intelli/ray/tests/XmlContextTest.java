package com.intelli.ray.tests;

import com.intelli.ray.Bean1;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import com.intelli.ray.core.XmlContext;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 11:38
 */
public class XmlContextTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:com/intelli/ray/context.xml");
        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();
        Bean1 bean = beanContainer.getBean(Bean1.class);
        assertNotNull(bean);
        assertNotNull(bean.getBean2());
    }
}
