package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;
import com.intelli.ray.meta.Profile;
import com.intelli.ray.reflection.ReflectionException;
import com.intelli.ray.reflection.Scanner;
import com.intelli.ray.util.Exceptions;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.intelli.ray.util.Reflections.*;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:11
 */
@InterfaceAudience.Public
public class AnnotationContext extends BaseContext implements ConfigurableContext {

    protected final String[] scanLocations;
    protected final AnnotationConfiguration configuration;

    /**
     * Builds the context by annotation scanning in specified locations.
     * Location order provides overriding.
     *
     * @param scanLocations - package paths
     */
    public AnnotationContext(String... scanLocations) {
        this(new DefaultAnnotationConfiguration(), scanLocations);
    }

    public AnnotationContext(AnnotationConfiguration configuration, String... scanLocations) {
        this.configuration = configuration;
        this.scanLocations = scanLocations;
    }

    @Override
    public AnnotationConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    protected void registerBeanDefinitions() {
        Scanner scanner = new Scanner(logger, contextClassLoader, scanLocations);
        try {
            scanner.scan();
        } catch (ReflectionException e) {
            logger.error(Exceptions.toStr("Failed to scan locations:", e));
            throw e;
        }

        AnnotationConfiguration configuration = getConfiguration();
        Iterable<Class<? extends Annotation>> annotations = configuration.getManagedComponentAnnotations();
        Map<Class, Class<? extends Annotation>> classMap = getTypesAnnotatedWith(scanner.getClasses(), annotations);
        AnnotationConfiguration.NameAndScopeExtractor nameAndScopeExtractor = configuration.getNameAndScopeExtractor();

        for (Map.Entry<Class, Class<? extends Annotation>> entry : classMap.entrySet()) {
            Class clazz = entry.getKey();
            Class<? extends Annotation> annotationClass = entry.getValue();

            Collection<String> beanProfiles = extractProfiles(clazz);
            if (!acceptProfiles(beanProfiles)) {
                logger.info("Skip registration of " + clazz + " - no profile match.");
                continue;
            }

            Annotation ann = clazz.getAnnotation(annotationClass);
            AnnotationConfiguration.NameAndScope nameAndScope = nameAndScopeExtractor.extract(ann);

            String beanId = !nameAndScope.name.isEmpty() ? nameAndScope.name : clazz.getName();
            Scope scope = nameAndScope.scope;

            Constructor constructor = getConstructorsAnnotatedWith(clazz, configuration.getManagedConstructorAnnotations());
            if (scope == Scope.SINGLETON && constructor != null) {
                throw new BeanInstantiationException(String.format(
                        "Failed to register %s : Managed constructor is not supported in singletons", clazz));
            }

            BeanDefinition definition;
            Method[] initMethods = filterByProfile(getUniqueMethodsAnnotatedWith(clazz, configuration.getInitMethodAnnotations()));
            Method[] destroyMethods = filterByProfile(getUniqueMethodsAnnotatedWith(clazz, configuration.getDestroyMethodAnnotations()));
            Field[] autowiredFields = filterByProfile(getFieldsAnnotatedWith(clazz, configuration.getAutowiredAnnotations()));
            if (constructor != null) {
                definition = new BeanDefinition(beanId, scope, clazz, constructor, initMethods,
                        destroyMethods, autowiredFields);
            } else {
                try {
                    definition = new BeanDefinition(beanId, scope, clazz, clazz.newInstance(), initMethods,
                            destroyMethods, autowiredFields);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new BeanInstantiationException(e);
                }
            }

            beanContainer.register(definition);
        }
    }

    protected Field[] filterByProfile(Field[] fields) {
        List<Field> result = new ArrayList<>();
        for (Field field : fields) {
            Collection<String> profiles = extractProfiles(field);
            if (profiles == null || acceptProfiles(profiles)) {
                result.add(field);
            }
        }
        return result.toArray(new Field[result.size()]);
    }

    protected Method[] filterByProfile(Method[] methods) {
        List<Method> result = new ArrayList<>();
        for (Method method : methods) {
            Collection<String> profiles = extractProfiles(method);
            if (profiles == null || acceptProfiles(profiles)) {
                result.add(method);
            }
        }
        return result.toArray(new Method[result.size()]);
    }

    protected Collection<String> extractProfiles(AnnotatedElement beanClass) {
        Profile profileAnn = beanClass.getAnnotation(Profile.class);
        if (profileAnn != null) {
            return Arrays.asList(profileAnn.value());
        } else {
            return null;
        }
    }
}
