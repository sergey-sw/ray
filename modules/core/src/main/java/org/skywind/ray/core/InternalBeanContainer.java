package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

/**
 * Extension of BeanContainer with internal methods.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 11:35
 */
@InterfaceAudience.Development
public interface InternalBeanContainer extends BeanContainer {

    BeanDefinition getBeanDefinition(Class clazz) throws BeanInstantiationException;

    void register(BeanDefinition beanDefinition);

    <T> T getBeanAnyScope(Class<T> beanClass) throws BeanNotFoundException;

    void autowireSingletons();

    void initSingletons();

    void destroyBeans();
}
