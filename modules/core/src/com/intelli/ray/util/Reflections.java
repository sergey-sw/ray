package com.intelli.ray.util;

import com.intelli.ray.meta.InterfaceAudience;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Author: Sergey42
 * Date: 24.05.14 21:18
 */
@InterfaceAudience.Development
public final class Reflections {

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

    public static Method[] getUniqueMethodsAnnotatedWith(Class clazz, Iterable<Class<? extends Annotation>> annotations) {
        Iterable<Method> allMethods = getAllMethods(clazz);
        List<Method> methods = new ArrayList<>(4);

        methodLoop:
        for (Method method : allMethods) {
            for (Class<? extends Annotation> annotation : annotations) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                    continue methodLoop;
                }
            }
        }

        int size = methods.size();
        return methods.toArray(new Method[size]);
    }

    public static Field[] getFieldsAnnotatedWith(Class clazz, Iterable<Class<? extends Annotation>> annotations) {
        Iterable<Field> allFields = getAllFields(clazz);
        List<Field> fields = new ArrayList<>();

        fieldLoop:
        for (Field field : allFields) {
            for (Class<? extends Annotation> annotation : annotations) {
                if (field.isAnnotationPresent(annotation)) {
                    fields.add(field);
                    continue fieldLoop;
                }
            }
        }

        return fields.toArray(new Field[fields.size()]);
    }

    public static Iterable<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    public static Iterable<Method> getAllMethods(Class clazz) {
        List<Method> methods = new ArrayList<>();

        while (clazz != Object.class) {
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            clazz = clazz.getSuperclass();
        }

        return methods;
    }

    public static Iterable<Method> getUniqueMethods(Class clazz) {
        List<Method> methods = new ArrayList<>();
        Set<String> names = new HashSet<>();

        while (clazz != Object.class) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (!names.contains(method.getName())) {
                    methods.add(method);
                    names.add(method.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }

        return methods;
    }
}
