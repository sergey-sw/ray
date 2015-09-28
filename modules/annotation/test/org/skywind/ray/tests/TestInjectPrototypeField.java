package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.core.AnnotationContext;
import org.skywind.ray.core.BeanContainer;
import org.skywind.ray.core.Context;
import org.skywind.ray.proto_test_scope.BeanWithInjections;

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
