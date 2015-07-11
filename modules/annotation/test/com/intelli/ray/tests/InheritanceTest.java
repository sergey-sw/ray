package com.intelli.ray.tests;

import com.intelli.ray.base_scope.ChildBean;
import com.intelli.ray.core.BeanContainer;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 18:05
 */
public class InheritanceTest extends AnnotationTest {

    public void test() throws Exception {
        BeanContainer beanContainer = context.getBeanContainer();
        ChildBean bean = beanContainer.getBean(ChildBean.class);

        assertNotNull(bean.getSingle());
        assertNotNull(bean.getBeanNoName());
    }
}
