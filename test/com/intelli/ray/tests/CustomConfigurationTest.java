package com.intelli.ray.tests;

import com.intelli.ray.base_scope.CustomBean;
import com.intelli.ray.base_scope.MBEAN;
import com.intelli.ray.base_scope.WIRE;
import com.intelli.ray.core.*;
import junit.framework.TestCase;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 15:35
 */
public class CustomConfigurationTest extends TestCase {

    public void test() throws Exception {
        ConfigurableContext context = new AnnotationBasedContext(configuration, "com.intelli.ray.base_scope");
        context.refresh();

        CustomBean bean = context.getBeanContainer().getBean(CustomBean.class);
        assertNotNull(bean.getSingle());

        for (Class<? extends Annotation> a : context.getConfiguration().getManagedConstructorAnnotations()) {
            System.out.println(a);
        }
    }

    Configuration configuration = new DefaultAnnotationConfiguration() {
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
