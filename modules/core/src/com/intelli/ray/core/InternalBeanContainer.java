package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Extension of BeanContainer with internal methods.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 11:35
 */
@InterfaceAudience.Development
public interface InternalBeanContainer extends BeanContainer {

    Iterable<Class> getManagedComponentClasses();

    BeanDefinition getBeanDefinition(Class beanClass);

    void register(BeanDefinition beanDefinition);

    <T> T getBeanAnyScope(Class<T> beanClass);

    void autowireSingletons();

    void initSingletons();

    void destroyBeans();
}
