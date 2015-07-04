package com.intelli.ray.core;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 12:20
 */
public abstract class BaseConfigurableContext extends BaseContext implements ConfigurableContext {

    private final Configuration configuration;

    protected BaseConfigurableContext(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
