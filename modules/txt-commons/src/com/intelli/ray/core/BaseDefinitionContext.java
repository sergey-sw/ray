package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;
import com.intelli.ray.resource.ResourceLoader;

/**
 * Base class for a contexts, that are built from text definitions, like xml and json.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 13:02
 */
@InterfaceAudience.Development
public abstract class BaseDefinitionContext extends BaseContext {

    protected BeanDefinitionConverter beanDefinitionConverter = createBeanDefinitionConverter();
    protected final ResourceLoader resourceLoader = new ResourceLoader();

    protected BeanDefinitionConverter createBeanDefinitionConverter() {
        return new BeanDefinitionConverter(contextClassLoader);
    }
}
