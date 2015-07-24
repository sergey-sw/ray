package com.intelli.ray.tests;

import com.intelli.ray.base_scope.AnnBeanNoName;
import com.intelli.ray.core.BeanDefinition;
import com.intelli.ray.core.Context;
import com.intelli.ray.core.InternalBeanContainer;
import com.intelli.ray.log.SimpleLogConsumer;
import junit.framework.TestCase;

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


        assertEquals("com.intelli.ray.base_scope.AnnBeanNoName", beanDefinition.getId());
    }
}
