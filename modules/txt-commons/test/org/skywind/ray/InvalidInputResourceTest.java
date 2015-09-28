package org.skywind.ray;

import org.junit.Test;
import org.skywind.ray.core.*;

import java.io.InputStream;
import java.util.Collections;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 24.07.2015 22:11
 */
public class InvalidInputResourceTest {

    @Test(expected = ContextStartupException.class)
    public void test() throws Exception {
        Context context = new InvalidResourceTestContext("dada");
        context.refresh();
    }

    static class InvalidResourceTestContext extends BaseDefinitionContext {

        protected InvalidResourceTestContext(String... locations) {
            super(locations);
        }

        @Override
        protected ContextReader createContextReader() {
            return new ContextReader() {
                @Override
                public ContextDescriptor readContext(InputStream inputStream) throws ReaderException {
                    return new ContextDescriptor(
                            Collections.<BeanDefinitionDescriptor>emptyList(),
                            Collections.<String>emptyList());
                }
            };
        }
    }
}
