package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 10.07.2015 18:48
 */
@InterfaceAudience.Public
public class XmlContext extends BaseDefinitionContext {

    /**
     * Creates XML application context from .xml configuration files
     *
     * @param xmlLocations locations of xml files
     */
    public XmlContext(String... xmlLocations) {
        super(xmlLocations);
    }

    @Override
    protected ContextReader createContextReader() {
        return new JdkXmlContextReader();
    }
}
