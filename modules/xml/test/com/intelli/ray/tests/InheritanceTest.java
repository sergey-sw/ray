package com.intelli.ray.tests;

import com.intelli.ray.ChildBean;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import com.intelli.ray.core.XmlContext;
import junit.framework.TestCase;


/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 18:05
 */
public class InheritanceTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:com/intelli/ray/context.xml");
        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();
        ChildBean bean = beanContainer.getBean(ChildBean.class);

        assertNotNull(bean.getSingle());
        assertNotNull(bean.getBeanNoName());
    }
}
