package com.intelli.ray.base_scope;

import com.intelli.ray.core.Configuration;
import com.intelli.ray.core.ManagedComponent;
import com.intelli.ray.core.Scope;

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

    public static Configuration.NameAndScopeExtractor extractor = new Configuration.NameAndScopeExtractor() {
        @Override
        public Configuration.NameAndScope extract(Annotation componentAnnotation) {
            if (componentAnnotation.annotationType().equals(ManagedComponent.class)) {
                ManagedComponent c = (ManagedComponent) componentAnnotation;
                return new Configuration.NameAndScope(c.name(), c.scope());
            }
            return new Configuration.NameAndScope("custom", Scope.SINGLETON);
        }
    };
}
