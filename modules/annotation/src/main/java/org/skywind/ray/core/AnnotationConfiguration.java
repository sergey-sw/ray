package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Configuration allows to encapsulate meta information specific details.
 * In annotation context managed components and their relations are declared with
 * annotations. By default, Ray uses {@link org.skywind.ray.meta.ManagedComponent}
 * for components, {@link org.skywind.ray.meta.Inject} for relations and so on.
 * These annotations are declared in {@link DefaultAnnotationConfiguration}.
 * <p/>
 * However, there is no restriction to use only these annotation for context.
 * It is possible to provide your own Configuration implementation and operate
 * with other types of annotations in your code.
 * <p/>
 * Be patient to provide correct annotation classes, since {@link java.lang.annotation.Target}
 * and {@link java.lang.annotation.Retention} can't be checked via generics.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 12:00
 */
@InterfaceAudience.Public
public interface AnnotationConfiguration {

    /**
     * Method declares what kind of annotations are used to indicate, that class
     * should be present in a context like a managed component.
     * See {@link org.skywind.ray.meta.ManagedComponent}
     */
    Iterable<Class<? extends Annotation>> getManagedComponentAnnotations();

    /**
     * Returns a NameAndScopeExtractor for this configuration.
     * See {@link AnnotationConfiguration.NameAndScopeExtractor}
     */
    NameAndScopeExtractor getNameAndScopeExtractor();

    /**
     * Method declares what kind of annotations are used to indicate that
     * constructor should be called for component instantiation.
     * See {@link org.skywind.ray.meta.ManagedConstructor}
     */
    Iterable<Class<? extends Annotation>> getManagedConstructorAnnotations();

    /**
     * Method declares what kind of annotations are used to indicate that
     * methods should be invoked on initialization phase.
     * See {@link javax.annotation.PostConstruct}
     */
    Iterable<Class<? extends Annotation>> getInitMethodAnnotations();

    /**
     * Method declares what kind of annotations are used to indicate that
     * methods should be invoked on destruction phase.
     * See {@link javax.annotation.PreDestroy}
     */
    Iterable<Class<? extends Annotation>> getDestroyMethodAnnotations();

    /**
     * Method declares what kind of annotations are used to indicate that
     * component fields should be autowired with values on initialization phase.
     * See {@link org.skywind.ray.meta.Inject}
     */
    Iterable<Class<? extends Annotation>> getAutowiredAnnotations();

    /**
     * POJO, that encapsulates managed component name and scope.
     */
    static class NameAndScope {
        public final String name;
        public final Scope scope;

        public NameAndScope(String name, Scope scope) {
            this.name = Objects.requireNonNull(name);
            this.scope = Objects.requireNonNull(scope);
        }
    }

    /**
     * Helper interface, that is used to extract information about
     * managed component name and scope from the annotation.
     */
    interface NameAndScopeExtractor {
        NameAndScope extract(Annotation componentAnnotation);
    }
}
