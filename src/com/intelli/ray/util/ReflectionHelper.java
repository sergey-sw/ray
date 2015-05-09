package com.intelli.ray.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Sergey42
 * Date: 24.05.14 21:18
 */
public class ReflectionHelper {

    public static Field[] getAllAnnotatedFields(Class clazz, Class<? extends Annotation> annotation) {
        Set<Field> fields = new HashSet<>();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotation)) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[fields.size()]);
    }
}
