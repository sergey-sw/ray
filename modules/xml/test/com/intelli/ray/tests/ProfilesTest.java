package com.intelli.ray.tests;

import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import com.intelli.ray.core.XmlContext;
import com.intelli.ray.log.SimpleLogConsumer;
import com.intelli.ray.profiles.B1;
import junit.framework.TestCase;

import java.util.Collection;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 17.07.2015 15:25
 */
public class ProfilesTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:com/intelli/ray/context-w-profiles.xml");
        context.getLoggerRegistry().addLogConsumer(new SimpleLogConsumer() {
            @Override
            public void log(String msg) {
                System.out.println(msg);
            }
        });
        context.refresh();

        Collection<String> profiles = context.getProfiles();
        assertFalse(profiles.isEmpty());
        assertTrue(profiles.contains("production"));

        BeanContainer beanContainer = context.getBeanContainer();
        assertTrue(beanContainer.containsBean("b1"));
        assertFalse(beanContainer.containsBean("b2"));
        assertTrue(beanContainer.containsBean("b3"));

        assertNull(beanContainer.getBean(B1.class).getB2());
        assertNotNull(beanContainer.getBean(B1.class).getB3());
    }
}
