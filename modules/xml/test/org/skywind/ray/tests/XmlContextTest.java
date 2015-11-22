package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.Bean1;
import org.skywind.ray.core.BeanContainer;
import org.skywind.ray.core.Context;
import org.skywind.ray.core.XmlContext;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 11:38
 */
public class XmlContextTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:org/skywind/ray/context.xml");
        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();
        Bean1 bean = beanContainer.getBean(Bean1.class);
        assertNotNull(bean);
        assertNotNull(bean.getBean2());
    }
}
