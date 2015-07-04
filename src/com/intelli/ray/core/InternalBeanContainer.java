package com.intelli.ray.core;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 11:35
 */
public interface InternalBeanContainer extends BeanContainer {

    BeanDefinition getBeanDefinition(Class beanClass);

    void register(BeanDefinition beanDefinition);

    void autowireSingletons();

    void initSingletons();

    void destroyBeans();
}
