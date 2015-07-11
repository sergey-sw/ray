package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;
import com.intelli.ray.resource.ResourceLoader;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.intelli.ray.core.BeanDefinitionDescriptor.*;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 13:17
 */
@InterfaceAudience.Private
public class JdkDomBridge implements DomBridge {

    private final ResourceLoader resourceLoader;

    public JdkDomBridge(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Iterable<BeanDefinitionDescriptor> extract(String location) {
        List<BeanDefinitionDescriptor> descriptors = new ArrayList<>();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = builder.parse(resourceLoader.open(location));
            Element mainElement = document.getDocumentElement();
            NodeList childNodes = mainElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (BEAN.equals(node.getNodeName())) {
                    NamedNodeMap attributes = node.getAttributes();

                    String clazz = attr(attributes, CLASS);
                    String id = attr(attributes, ID, clazz);
                    String scope = attr(attributes, SCOPE);

                    String initMethods = attr(attributes, INIT_METHODS);
                    String destroyMethods = attr(attributes, DESTROY_METHODS);
                    String autowireFields = attr(attributes, AUTOWIRED);

                    BeanDefinitionDescriptor descriptor = new BeanDefinitionDescriptor(id, clazz, scope,
                            autowireFields, initMethods, destroyMethods);

                    descriptors.add(descriptor);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalArgumentException("Could not parse source : " + location, e);
        }

        return descriptors;
    }

    private String attr(NamedNodeMap attributes, String name) {
        return attr(attributes, name, null);
    }

    private String attr(NamedNodeMap attributes, String name, String def) {
        Node namedItem = attributes.getNamedItem(name);
        return namedItem != null ? namedItem.getNodeValue() : def;
    }
}
