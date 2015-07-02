package com.intelli.ray.tests;

import com.intelli.ray.base_scope.BeanWithPostConstruct1;
import com.intelli.ray.base_scope.BeanWithPostConstruct2;
import com.intelli.ray.core.BeanContainer;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 23:26
 */
public class PostConstructTest extends RayTest {

    public void test() throws Exception {
        BeanContainer beanContainer = context.getBeanContainer();
        BeanWithPostConstruct2 bean = beanContainer.getBean(BeanWithPostConstruct2.class);

        assertEquals(bean.getField1(), 1);
        assertEquals(bean.getField2(), 4);

        BeanWithPostConstruct1 b1 = beanContainer.getBean(BeanWithPostConstruct1.class);
        assertEquals(b1.getField2(), 2);
    }
}
