package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;
import org.skywind.ray.resource.ResourceLoader;
import org.skywind.ray.util.IO;

import java.io.InputStream;
import java.util.Objects;

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

    protected final String[] locations;

    protected BaseDefinitionContext(String[] locations) {
        this.locations = Objects.requireNonNull(locations);
    }

    protected abstract ContextReader createContextReader();

    protected BeanDefinitionConverter createBeanDefinitionConverter() {
        return new BeanDefinitionConverter(contextClassLoader);
    }

    @Override
    protected void registerBeanDefinitions() {
        for (String location : locations) {
            InputStream stream = resourceLoader.open(location);
            if (stream == null) {
                throw new ContextStartupException(String.format("Failed to read resource '%s'", location));
            }

            try {
                ContextReader contextReader = createContextReader();
                ContextDescriptor contextDescriptor = contextReader.readContext(stream);
                applyContextDescriptor(contextDescriptor);
            } catch (ContextReader.ReaderException e) {
                throw new ContextStartupException(String.format("Failed to read resource '%s'", location), e);
            } finally {
                IO.close(stream);
            }
        }
    }

    protected void applyContextDescriptor(ContextDescriptor contextDescriptor) {
        setProfiles(contextDescriptor.getProfiles());

        for (BeanDefinitionDescriptor descriptor : contextDescriptor.getBeanDefinitionDescriptors()) {
            if (acceptProfiles(descriptor.profiles)) {
                BeanDefinition beanDefinition = beanDefinitionConverter.convert(descriptor);
                beanContainer.register(beanDefinition);
            } else {
                logger.info("Skip registration of " + descriptor.clazz + " - no profile match.");
            }
        }
    }
}
