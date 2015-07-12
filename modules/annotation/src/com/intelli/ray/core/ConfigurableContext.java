package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Extension of Context, that allows to configure meta information
 * specifics, that are used during context initialization.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 12:15
 */
@InterfaceAudience.Development
public interface ConfigurableContext extends Context {

    /**
     * Returns instance of Configuration, that will be used
     * during context initialization.
     * See {@link AnnotationConfiguration}
     *
     * @return configuration
     */
    AnnotationConfiguration getConfiguration();
}
