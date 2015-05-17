package com.intelli.ray.tests;

import com.intelli.ray.core.AnnotationBasedContext;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import com.intelli.ray.proto_test_scope.BeanWithInjections;

/**
 * Author: Sergey42
 * Date: 17.05.2015 17:04
 */
public class TestInjectPrototypeField extends RayTest {

    public void test() throws Exception {
        Context protoCtx = new AnnotationBasedContext("com.intelli.ray.proto_test_scope");
        protoCtx.start();

        BeanContainer container = protoCtx.getBeanContainer();
        BeanWithInjections bean = container.getBean(BeanWithInjections.class);

        assertNotNull(bean.getSingle());

        assertNotNull(bean.getPrototypeBean());
    }
}
