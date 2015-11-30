package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

import java.util.Collection;
import java.util.Objects;

/**
 * Encapsulates context information, that can be extracted from context definition.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 17.07.2015 16:25
 */
@InterfaceAudience.Development
public class ContextDescriptor {

    private final Collection<BeanDefinitionDescriptor> beanDefinitionDescriptors;
    private final Collection<String> profiles;

    public ContextDescriptor(Collection<BeanDefinitionDescriptor> descriptors, Collection<String> profiles) {
        this.beanDefinitionDescriptors = Objects.requireNonNull(descriptors);
        this.profiles = Objects.requireNonNull(profiles);
    }

    public Collection<String> getProfiles() {
        return profiles;
    }

    public Collection<BeanDefinitionDescriptor> getBeanDefinitionDescriptors() {
        return beanDefinitionDescriptors;
    }
}
