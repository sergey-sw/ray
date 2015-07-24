package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.intelli.ray.core.BeanDefinitionDescriptor.*;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 13:17
 */
@InterfaceAudience.Private
public class JdkXmlContextReader implements ContextReader {

    @Override
    public ContextDescriptor readContext(InputStream inputStream) {
        List<BeanDefinitionDescriptor> descriptors = new ArrayList<>();
        Set<String> activeProfiles = new HashSet<>();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = builder.parse(inputStream);
            Element mainElement = document.getDocumentElement();
            NodeList childNodes = mainElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (BEAN.equals(node.getNodeName())) {
                    descriptors.add(parseBean(node));
                } else if (PROFILES.equals(node.getNodeName())) {
                    activeProfiles.addAll(parseProfiles(node));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ReaderException(e);
        }

        return new ContextDescriptor(descriptors, activeProfiles);
    }

    private BeanDefinitionDescriptor parseBean(Node node) {
        NamedNodeMap attributes = node.getAttributes();

        String clazz = attr(attributes, CLASS);
        String id = attr(attributes, ID, clazz);
        String scope = attr(attributes, SCOPE);

        String initMethods = attr(attributes, INIT_METHODS);
        String destroyMethods = attr(attributes, DESTROY_METHODS);
        String autowireFields = attr(attributes, AUTOWIRED);
        String profiles = attr(attributes, PROFILES);

        return new BeanDefinitionDescriptor(id, clazz, scope, autowireFields, initMethods, destroyMethods, profiles);
    }

    private Set<String> parseProfiles(Node node) {
        Set<String> profiles = new HashSet<>();
        NodeList profileNodes = node.getChildNodes();
        for (int i = 0; i < profileNodes.getLength(); i++) {
            Node profileNode = profileNodes.item(i);

            if (PROFILE.equals(profileNode.getNodeName())) {
                NamedNodeMap attributes = profileNode.getAttributes();
                String name = attr(attributes, NAME);
                String active = attr(attributes, ACTIVE, "true");
                if (Boolean.valueOf(active)) {
                    profiles.add(name);
                }
            }
        }
        return profiles;
    }

    private String attr(NamedNodeMap attributes, String name) {
        return attr(attributes, name, null);
    }

    private String attr(NamedNodeMap attributes, String name, String def) {
        Node namedItem = attributes.getNamedItem(name);
        return namedItem != null ? namedItem.getNodeValue() : def;
    }
}
