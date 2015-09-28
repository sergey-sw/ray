package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.core.Context;

/**
 * Author: Sergey42
 * Date: 16.11.13 17:06
 */
public class TestContextStart extends TestCase {

    public void test() {
        Context context = new JsonTestContext();

        assertFalse(context.isActive());

        context.refresh();

        assertTrue(context.isActive());
    }
}
