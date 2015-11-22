package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.base_scope.CustomBean;
import org.skywind.ray.base_scope.MBEAN;
import org.skywind.ray.base_scope.WIRE;
import org.skywind.ray.core.AnnotationConfiguration;
import org.skywind.ray.core.AnnotationContext;
import org.skywind.ray.core.ConfigurableContext;
import org.skywind.ray.core.DefaultAnnotationConfiguration;
import org.skywind.ray.meta.ManagedComponent;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 15:35
 */
public class CustomConfigurationTest extends TestCase {

    public void test() throws Exception {
        ConfigurableContext context = new AnnotationContext(configuration, "org.skywind.ray.base_scope");
        context.refresh();

        CustomBean bean = context.getBeanContainer().getBean(CustomBean.class);
        assertNotNull(bean.getSingle());

        for (Class<? extends Annotation> a : context.getConfiguration().getManagedConstructorAnnotations()) {
            System.out.println("Managed const annotation: " + a);
        }
    }

    AnnotationConfiguration configuration = new DefaultAnnotationConfiguration() {
        @Override
        public Iterable<Class<? extends Annotation>> getManagedComponentAnnotations() {
            return Arrays.asList(MBEAN.class, ManagedComponent.class);
        }

        @Override
        public NameAndScopeExtractor getNameAndScopeExtractor() {
            return CustomBean.extractor;
        }

        @Override
        public Iterable<Class<? extends Annotation>> getAutowiredAnnotations() {
            return Arrays.<Class<? extends Annotation>>asList(WIRE.class);
        }
    };
}
