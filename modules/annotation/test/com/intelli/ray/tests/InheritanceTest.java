package com.intelli.ray.tests;

import com.intelli.ray.base_scope.ChildBean;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 18:05
 */
public class InheritanceTest extends TestCase {

    public void test() throws Exception {
        Context context = new TestAnnotationContext();
        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();
        ChildBean bean = beanContainer.getBean(ChildBean.class);

        assertNotNull(bean.getSingle());
        assertNotNull(bean.getAnnBeanNoName());
    }
}
