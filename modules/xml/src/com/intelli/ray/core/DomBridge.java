package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 12:42
 */
@InterfaceAudience.Development
public interface DomBridge {

    Iterable<BeanDefinitionDescriptor> extract(String location);
}
