package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.BeanWithManagedConstructor;
import org.skywind.ray.core.Context;

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
