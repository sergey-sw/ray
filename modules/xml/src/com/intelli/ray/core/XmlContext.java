package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

import java.util.Objects;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 10.07.2015 18:48
 */
@InterfaceAudience.Public
public class XmlContext extends BaseDefinitionContext {

    protected final String[] locations;
    protected final DomBridge domBridge = new JdkDomBridge(resourceLoader);

    public XmlContext(String... locations) {
        this.locations = Objects.requireNonNull(locations);
    }

    @Override
    protected void registerBeanDefinitions() {
        for (String location : locations) {
            Iterable<BeanDefinitionDescriptor> descriptors = domBridge.extract(location);
            for (BeanDefinitionDescriptor descriptor : descriptors) {
                BeanDefinition beanDefinition = beanDefinitionConverter.convert(descriptor);
                beanContainer.register(beanDefinition);
            }
        }
    }
}
