package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;
import org.skywind.ray.util.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides bean definition model conversion from string representation
 * to object representation.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 12:48
 */
@InterfaceAudience.Development
public class BeanDefinitionConverter {

    protected final ClassLoader classLoader;

    public BeanDefinitionConverter(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public BeanDefinition convert(BeanDefinitionDescriptor descriptor) {
        List<Field> autowiredFieldList = new ArrayList<>();
        List<Method> pcMethodList = new ArrayList<>();
        List<Method> pdMethodList = new ArrayList<>();

        Class beanClass;
        Object instance;
        Field[] autowiredFields;
        Method[] initMethods;
        Method[] destroyMethods;
        try {
            beanClass = classLoader.loadClass(descriptor.clazz);
            instance = beanClass.newInstance();

            if (descriptor.autowired != null) {
                Iterable<Field> allFields = extractFields(beanClass);
                for (String fieldName : descriptor.autowired) {
                    for (Field declaredField : allFields) {
                        if (fieldName.equals(declaredField.getName())) {
                            autowiredFieldList.add(declaredField);
                        }
                    }
                }
            }
            autowiredFields = autowiredFieldList.toArray(new Field[autowiredFieldList.size()]);

            Iterable<Method> allMethods = null;
            if (descriptor.initMethods != null) {
                allMethods = extractMethods(beanClass);
                for (String initMethodName : descriptor.initMethods) {
                    for (Method declaredMethod : allMethods) {
                        if (initMethodName.equals(declaredMethod.getName())) {
                            pcMethodList.add(declaredMethod);
                        }
                    }
                }
            }
            initMethods = pcMethodList.toArray(new Method[pcMethodList.size()]);

            if (descriptor.destroyMethods != null) {
                if (allMethods == null) {
                    allMethods = extractMethods(beanClass);
                }
                for (String destroyMethodName : descriptor.destroyMethods) {
                    for (Method declaredMethod : allMethods) {
                        if (destroyMethodName.equals(declaredMethod.getName())) {
                            pdMethodList.add(declaredMethod);
                        }
                    }
                }
            }
            destroyMethods = pdMethodList.toArray(new Method[pdMethodList.size()]);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }

        return new BeanDefinition(
                descriptor.id,
                Scope.defaultScope(Scope.fromId(descriptor.scope)),
                beanClass,
                instance,
                initMethods,
                destroyMethods,
                autowiredFields);
    }

    protected Iterable<Method> extractMethods(Class clazz) {
        return Reflections.getUniqueMethods(clazz);
    }

    protected Iterable<Field> extractFields(Class clazz) {
        return Reflections.getAllFields(clazz);
    }
}
