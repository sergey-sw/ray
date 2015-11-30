package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.core.AnnotationContext;
import org.skywind.ray.core.Context;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 07.11.2015 19:49
 */
public class InterfaceInjectionTest extends TestCase {

    public void test() throws Exception {
        Context context = new AnnotationContext("org.skywind.ray.base_scope");
        context.refresh();

        //todo
        //MyInterface bean = context.getBeanContainer().getBean(MyInterface.class);
        //assertNotNull(bean);
    }
}
