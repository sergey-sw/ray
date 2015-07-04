package com.intelli.ray.reflection;

import com.intelli.ray.meta.InterfaceAudience;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Sergey42
 * Date: 24.05.14 21:18
 */
@InterfaceAudience.Development
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
        List<Method> methods = new ArrayList<>(4);
        List<String> methodNames = new ArrayList<>(4);

        while (clazz != Object.class) {
            Method[] classMethods = clazz.getDeclaredMethods();
            methodLoop:
            for (Method method : classMethods) {
                for (Class<? extends Annotation> annotation : annotations) {
                    if (method.isAnnotationPresent(annotation) && !methodNames.contains(method.getName())) {
                        methods.add(method);
                        methodNames.add(method.getName());
                        continue methodLoop;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        int size = methods.size();
        return methods.toArray(new Method[size]);
    }

    public static Field[] getFieldsAnnotatedWith(Class clazz, Iterable<Class<? extends Annotation>> annotations) {
        List<Field> fields = new ArrayList<>();

        while (clazz != Object.class) {
            Field[] classFields = clazz.getDeclaredFields();
            fieldLoop:
            for (Field field : classFields) {
                for (Class<? extends Annotation> annotation : annotations) {
                    if (field.isAnnotationPresent(annotation)) {
                        fields.add(field);
                        continue fieldLoop;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        int size = fields.size();
        return fields.toArray(new Field[size]);
    }
}
