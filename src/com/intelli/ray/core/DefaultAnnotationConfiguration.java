package com.intelli.ray.core;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 12:07
 */
public class DefaultAnnotationConfiguration implements Configuration {

    private final NameAndScopeExtractor nameAndScopeExtractor = new NameAndScopeExtractor() {
        @Override
        public NameAndScope extract(Annotation componentAnnotation) {
            ManagedComponent annotation = (ManagedComponent) componentAnnotation;
            return new NameAndScope(annotation.name(), annotation.scope());
        }
    };

    @Override
    public Iterable<Class<? extends Annotation>> getManagedComponentAnnotations() {
        return Arrays.<Class<? extends Annotation>>asList(ManagedComponent.class);
    }

    @Override
    public NameAndScopeExtractor getNameAndScopeExtractor() {
        return nameAndScopeExtractor;
    }

    @Override
    public Iterable<Class<? extends Annotation>> getManagedConstructorAnnotations() {
        return Arrays.<Class<? extends Annotation>>asList(ManagedConstructor.class);
    }

    @Override
    public Iterable<Class<? extends Annotation>> getInitMethodAnnotations() {
        return Arrays.<Class<? extends Annotation>>asList(PostConstruct.class);
    }

    @Override
    public Iterable<Class<? extends Annotation>> getAutowiredAnnotations() {
        return Arrays.<Class<? extends Annotation>>asList(Inject.class);
    }
}
