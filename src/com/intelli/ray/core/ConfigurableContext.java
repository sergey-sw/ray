package com.intelli.ray.core;

/**
 * Extension of Context, that allows to configure meta information
 * specifics, that are used during context initialization.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 12:15
 */
public interface ConfigurableContext extends Context {

    /**
     * Returns instance of Configuration, that will be used
     * during context initialization.
     * See {@link com.intelli.ray.core.Configuration}
     *
     * @return configuration
     */
    Configuration getConfiguration();
}
