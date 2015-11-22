package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.core.BeanContainer;
import org.skywind.ray.core.Context;
import org.skywind.ray.core.XmlContext;
import org.skywind.ray.log.SimpleLogConsumer;
import org.skywind.ray.profiles.B1;

import java.util.Collection;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 17.07.2015 15:25
 */
public class ProfilesTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:org/skywind/ray/context-w-profiles.xml");
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
