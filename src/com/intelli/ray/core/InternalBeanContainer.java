package com.intelli.ray.core;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 11:35
 */
public interface InternalBeanContainer extends BeanContainer {

    <T> T getBeanAnyScope(Class<T> beanClass);

    Iterable<BeanDefinition> getBeanDefinitions();

    BeanDefinition getBeanDefinition(Class beanClass);

    void register(BeanDefinition beanDefinition);

    void invokeInitMethods(BeanDefinition beanDefinition);

    void invokeInitMethods(BeanDefinition beanDefinition, Object instance);

    void destroyBeans();
}
