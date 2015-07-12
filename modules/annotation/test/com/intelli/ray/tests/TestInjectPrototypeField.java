package com.intelli.ray.tests;

import com.intelli.ray.core.AnnotationContext;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import com.intelli.ray.proto_test_scope.BeanWithInjections;
import junit.framework.TestCase;

/**
 * Author: Sergey42
 * Date: 17.05.2015 17:04
 */
public class TestInjectPrototypeField extends TestCase {

    public void test() throws Exception {
        Context protoCtx = new AnnotationContext("com.intelli.ray.proto_test_scope");
        protoCtx.refresh();

        BeanContainer container = protoCtx.getBeanContainer();
        BeanWithInjections bean = container.getBean(BeanWithInjections.class);

        assertNotNull(bean.getSingle());

        assertNotNull(bean.getPrototypeBean());
    }
}
