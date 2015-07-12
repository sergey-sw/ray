package com.intelli.ray.tests;

import com.intelli.ray.base_scope.BeanNoName;
import com.intelli.ray.core.BeanDefinition;
import com.intelli.ray.core.Context;
import com.intelli.ray.core.InternalBeanContainer;
import junit.framework.TestCase;

/**
 * Author: Sergey42
 * Date: 17.05.2015 20:58
 */
public class TestBeanIdGeneration extends TestCase {

    public void test() throws Exception {
        Context context = new TestAnnotationContext();
        context.refresh();
        InternalBeanContainer container = (InternalBeanContainer) context.getBeanContainer();
        BeanDefinition beanDefinition = container.getBeanDefinition(BeanNoName.class);

        assertNotNull(beanDefinition.getId());
        assertFalse(beanDefinition.getId().isEmpty());


        assertEquals("com.intelli.ray.base_scope.BeanNoName", beanDefinition.getId());
    }
}
