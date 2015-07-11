package com.intelli.ray.tests;

import com.intelli.ray.base_scope.JsonSingle;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 14:15
 */
public class SimpleTest extends TestCase {

    public void test() throws Exception {
        Context context = new JsonTestContext();

        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();
        JsonSingle jsonSingle = beanContainer.getBean("single");

        assertNotNull(jsonSingle.getBeanNoName());
    }


}
