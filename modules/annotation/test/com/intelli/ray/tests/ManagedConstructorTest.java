package com.intelli.ray.tests;

import com.intelli.ray.base_scope.BeanWithManagedConstructor;
import com.intelli.ray.core.Context;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 15:30
 */
public class ManagedConstructorTest extends TestCase {

    public void test() throws Exception {
        Context context = new TestAnnotationContext();
        context.refresh();
        BeanWithManagedConstructor bean = context.getBeanContainer().createPrototype(BeanWithManagedConstructor.class, 6);
        assertNotNull(bean.getField());
    }
}
