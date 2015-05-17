package com.intelli.ray.tests;

import com.intelli.ray.base_scope.BeanNoName;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.BeanDefinition;

/**
 * Author: Sergey42
 * Date: 17.05.2015 20:58
 */
public class TestBeanIdGeneration extends RayTest {

    public void test() throws Exception {
        BeanContainer container = context.getBeanContainer();
        BeanDefinition beanDefinition = container.getBeanDefinition(BeanNoName.class);

        assertNotNull(beanDefinition.getId());
        assertFalse(beanDefinition.getId().isEmpty());


        assertEquals("com.intelli.ray.base_scope.BeanNoName", beanDefinition.getId());
    }
}
