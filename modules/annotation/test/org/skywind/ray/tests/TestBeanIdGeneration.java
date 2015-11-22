package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.AnnBeanNoName;
import org.skywind.ray.core.BeanDefinition;
import org.skywind.ray.core.Context;
import org.skywind.ray.core.InternalBeanContainer;
import org.skywind.ray.log.SimpleLogConsumer;

/**
 * Author: Sergey42
 * Date: 17.05.2015 20:58
 */
public class TestBeanIdGeneration extends TestCase {

    public void test() throws Exception {
        System.out.println("start test id gen");
        Context context = new TestAnnotationContext();
        context.getLoggerRegistry().addLogConsumer(new SimpleLogConsumer() {
            @Override
            public void log(String msg) {
                System.out.println(msg);
            }
        });
        context.refresh();
        InternalBeanContainer container = (InternalBeanContainer) context.getBeanContainer();
        BeanDefinition beanDefinition = container.getBeanDefinition(AnnBeanNoName.class);

        assertNotNull(beanDefinition);
        assertNotNull(beanDefinition.getId());
        assertFalse(beanDefinition.getId().isEmpty());


        assertEquals("org.skywind.ray.base_scope.AnnBeanNoName", beanDefinition.getId());
    }
}
