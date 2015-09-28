package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

/**
 * Interface that encapsulate the process of component creation,
 * initialization (autowiring and init methods) and destruction;
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 19:12
 */
@InterfaceAudience.Development
public interface BeanLifecycleProcessor {

    /**
     * Performs autowiring of managed fields in a component.
     *
     * @param instance   managed component instance
     * @param definition bean definition
     */
    void autowireFields(Object instance, BeanDefinition definition);

    /**
     * Performs managed component initialization by invoking its init methods.
     *
     * @param instance       managed component instance
     * @param beanDefinition bean definition
     */
    void invokeInitMethods(Object instance, BeanDefinition beanDefinition);

    /**
     * Performs managed component pre-destruction by invoking its destroy methods
     *
     * @param instance       managed component instance
     * @param beanDefinition bean definition
     */
    void invokeDestroyMethods(Object instance, BeanDefinition beanDefinition);
}
