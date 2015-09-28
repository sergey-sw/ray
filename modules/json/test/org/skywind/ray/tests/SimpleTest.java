package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.JsonSingle;
import org.skywind.ray.core.BeanContainer;
import org.skywind.ray.core.Context;

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
