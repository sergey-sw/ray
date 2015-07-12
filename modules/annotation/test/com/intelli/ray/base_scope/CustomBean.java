package com.intelli.ray.base_scope;

import com.intelli.ray.core.AnnotationConfiguration;
import com.intelli.ray.core.Scope;
import com.intelli.ray.meta.ManagedComponent;

import java.lang.annotation.Annotation;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 15:35
 */

@MBEAN
public class CustomBean {

    @WIRE
    Single single;

    public Single getSingle() {
        return single;
    }

    public static AnnotationConfiguration.NameAndScopeExtractor extractor = new AnnotationConfiguration.NameAndScopeExtractor() {
        @Override
        public AnnotationConfiguration.NameAndScope extract(Annotation componentAnnotation) {
            if (componentAnnotation.annotationType().equals(ManagedComponent.class)) {
                ManagedComponent c = (ManagedComponent) componentAnnotation;
                return new AnnotationConfiguration.NameAndScope(c.name(), c.scope());
            }
            return new AnnotationConfiguration.NameAndScope("custom", Scope.SINGLETON);
        }
    };
}
