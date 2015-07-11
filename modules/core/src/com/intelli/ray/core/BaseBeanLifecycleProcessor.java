package com.intelli.ray.core;

import com.intelli.ray.log.ContextLogger;
import com.intelli.ray.meta.InterfaceAudience;
import com.intelli.ray.util.Exceptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base implementation for bean lifecycle processor
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 19:14
 */
@InterfaceAudience.Private
public class BaseBeanLifecycleProcessor implements BeanLifecycleProcessor {

    private final InternalBeanContainer beanContainer;
    private final ContextLogger logger;

    public BaseBeanLifecycleProcessor(InternalBeanContainer beanContainer, ContextLogger logger) {
        this.beanContainer = beanContainer;
        this.logger = logger;
    }

    @Override
    public void invokeInitMethods(Object instance, BeanDefinition beanDefinition) {
        Method[] initMethods = beanDefinition.initMethods;

        for (int i = initMethods.length - 1; i >= 0; i--) {
            Method method = initMethods[i];
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                logger.debug(String.format("Invoking init method %s in bean %s",
                        method.getName(), beanDefinition));
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                String msg = "Failed to execute post construct method of bean " + beanDefinition.beanClass.getName();
                logger.error(Exceptions.toStr(msg, e));
                throw new BeanInstantiationException(msg, e);
            }
        }
    }

    @Override
    public void invokeDestroyMethods(Object instance, BeanDefinition definition) {
        if (definition.destroyMethods != null) {
            for (Method destroyMethod : definition.destroyMethods) {
                logger.debug(String.format("Invoking pre-destroy method %s in bean %s",
                        destroyMethod.getName(), definition));
                try {
                    if (!destroyMethod.isAccessible()) {
                        destroyMethod.setAccessible(true);
                    }
                    destroyMethod.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error(Exceptions.toStr("Exception in pre-destroy method:", e));
                }
            }
        }
    }

    @Override
    public void autowireFields(Object instance, BeanDefinition definition) {
        Field[] fields = definition.autowiredFields;

        for (Field field : fields) {
            autowireField(instance, field, definition);
        }
    }

    protected void autowireField(Object instance, Field field, BeanDefinition definition) {
        Class fieldClazz = field.getType();
        BeanDefinition fieldBeanDefinition = beanContainer.getBeanDefinition(fieldClazz);
        if (fieldBeanDefinition == null) {
            for (Class clazz : beanContainer.getManagedComponentClasses()) {
                //noinspection unchecked
                if (fieldClazz.isAssignableFrom(clazz)) {
                    fieldBeanDefinition = beanContainer.getBeanDefinition(clazz);
                    break;
                }
            }
            if (fieldBeanDefinition == null) {
                throw new BeanInstantiationException("Can not inject property '" + field.getName() + "' in bean " +
                        definition.beanClass.getName() + ", because property bean class " + fieldClazz.getName()
                        + " is not present in context.");
            }
        }
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(instance, beanContainer.getBeanAnyScope(fieldBeanDefinition.beanClass));
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }
}
