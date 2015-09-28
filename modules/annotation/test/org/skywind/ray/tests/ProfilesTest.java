package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.AnnBeanNoName;
import org.skywind.ray.base_scope.TestEnvBean;
import org.skywind.ray.core.BeanContainer;
import org.skywind.ray.core.Context;

import java.util.Arrays;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 12.07.2015 17:03
 */
public class ProfilesTest extends TestCase {

    public void test() throws Exception {
        Context context = new TestAnnotationContext();
        context.setProfiles(Arrays.asList("test"));

        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();

        assertTrue(beanContainer.containsBean(TestEnvBean.class));
        assertFalse(beanContainer.containsBean(AnnBeanNoName.class));

        TestEnvBean bean = beanContainer.getBean(TestEnvBean.class);

        assertNotNull(bean.getTestEnvBean2());
        assertNull(bean.getSingle());
        assertNull(bean.getTestEnvBean3());
        assertNull(bean.getTeb3());

        assertEquals(1, bean.getA1());
        assertEquals(0, bean.getA2());
    }
}
