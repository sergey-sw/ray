package com.intelli.ray.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author: Sergey42
 * Date: 24.05.14 21:18
 */
public class ReflectionHelper {

    public static Map<Class, Class<? extends Annotation>> getTypesAnnotatedWith(
            Iterable<Class> classes,
            Iterable<Class<? extends Annotation>> annotations) {

        Map<Class, Class<? extends Annotation>> result = new HashMap<>();
        classLoop:
        for (Class clazz : classes) {
            for (Class<? extends Annotation> annotation : annotations) {
                if (clazz.isAnnotationPresent(annotation)) {
                    result.put(clazz, annotation);
                    continue classLoop;
                }
            }

        }
        return result;
    }

    public static Field[] getAllAnnotatedFields(Class clazz, Iterable<Class<? extends Annotation>> annotations) {
        Set<Field> fields = new HashSet<>();
        while (clazz != Object.class) {
            fieldLoop:
            for (Field field : clazz.getDeclaredFields()) {
                for (Class<? extends Annotation> annotation : annotations) {
                    if (field.isAnnotationPresent(annotation)) {
                        fields.add(field);
                        continue fieldLoop;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    public static Constructor getConstructorsAnnotatedWith(Class clazz, Iterable<Class<? extends Annotation>> annotations) {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            for (Class<? extends Annotation> annotation : annotations) {
                if (constructor.isAnnotationPresent(annotation)) {
                    return constructor;
                }
            }
        }
        return null;
    }
}
