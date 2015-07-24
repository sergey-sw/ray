package com.intelli.ray.core;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.intelli.ray.meta.InterfaceAudience;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static com.intelli.ray.core.BeanDefinitionDescriptor.*;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 17.07.2015 17:30
 */
@InterfaceAudience.Development
public class JsonContextReader implements ContextReader {

    @Override
    public ContextDescriptor readContext(InputStream stream) {
        JsonObject jsonObject = readStream(stream);

        JsonArray profilesJson = (JsonArray) jsonObject.get(PROFILES);
        Set<String> profiles = new HashSet<>();
        if (profilesJson != null) {
            for (JsonValue profile : profilesJson) {
                String profileName = profile.asString();
                if (profileName != null && !profileName.isEmpty()) {
                    profiles.add(profileName);
                }
            }
        }

        JsonArray jsonBeans = (JsonArray) Objects.requireNonNull(jsonObject.get(BEANS),
                "Beans definition element is missing in context descriptor");

        List<BeanDefinitionDescriptor> descriptors = new ArrayList<>();
        for (JsonValue jsonBean : jsonBeans) {
            descriptors.add(parseDescriptor(jsonBean));
        }
        return new ContextDescriptor(descriptors, profiles);
    }

    protected BeanDefinitionDescriptor parseDescriptor(JsonValue jsonBean) {
        JsonObject jsBean = jsonBean.asObject();
        JsonValue tmp;

        String clazz = jsBean.get(CLASS).asString();
        String id = (tmp = jsBean.get(ID)) != null ? tmp.asString() : clazz;
        String scope = (tmp = jsBean.get(SCOPE)) != null ? tmp.asString() : Scope.defaultScope();

        JsonArray empty = new JsonArray();
        JsonArray autowiredFieldsJson = (tmp = jsBean.get(AUTOWIRED)) != null ? tmp.asArray() : empty;
        JsonArray postConstructJson = (tmp = jsBean.get(INIT_METHODS)) != null ? tmp.asArray() : empty;
        JsonArray preDestroyJson = (tmp = jsBean.get(DESTROY_METHODS)) != null ? tmp.asArray() : empty;
        JsonArray profilesJson = (tmp = jsBean.get(PROFILES)) != null ? tmp.asArray() : empty;

        Set<String> autowiredFieldNames = new LinkedHashSet<>();
        for (JsonValue autowiredField : autowiredFieldsJson) {
            autowiredFieldNames.add(autowiredField.asString());
        }

        Set<String> initMethodNames = new LinkedHashSet<>();
        for (JsonValue pcMethod : postConstructJson) {
            initMethodNames.add(pcMethod.asString());
        }

        Set<String> destroyMethodNames = new LinkedHashSet<>();
        for (JsonValue pdMethod : preDestroyJson) {
            destroyMethodNames.add(pdMethod.asString());
        }
        Set<String> profiles = new HashSet<>();
        for (JsonValue profileJson : profilesJson) {
            profiles.add(profileJson.asString());
        }

        return new BeanDefinitionDescriptor(id, clazz, scope,
                autowiredFieldNames, initMethodNames, destroyMethodNames, profiles);
    }

    protected JsonObject readStream(InputStream stream) {
        InputStreamReader isr = new InputStreamReader(stream);
        try {
            return JsonObject.readFrom(isr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
