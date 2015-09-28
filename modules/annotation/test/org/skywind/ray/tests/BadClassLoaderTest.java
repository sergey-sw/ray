package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.core.AnnotationContext;
import org.skywind.ray.core.Context;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 15:19
 */
public class BadClassLoaderTest extends TestCase {

    public void test() throws Exception {
        Context context = new AnnotationContext("com.intelli.ray.base_scope", "com.intelli.ray.lonely_package");
        context.setClassLoader(new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                throw new RuntimeException(name);
            }
        });

        try {
            context.refresh();
        } catch (RuntimeException ignored) {
            return;
        }
        fail();
    }

}
