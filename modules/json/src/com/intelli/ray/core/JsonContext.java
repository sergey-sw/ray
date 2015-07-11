package com.intelli.ray.core;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static com.intelli.ray.core.BeanDefinitionDescriptor.*;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 05.07.2015 19:23
 */
public class JsonContext extends BaseDefinitionContext {

    public static final String BEANS = "beans";

    protected final String[] jsonLocations;

    public JsonContext(String... jsonLocations) {
        this.jsonLocations = Objects.requireNonNull(jsonLocations);
    }

    @Override
    protected void registerBeanDefinitions() {
        try {
            for (String jsonLocation : jsonLocations) {
                InputStream stream = resourceLoader.open(jsonLocation);

                JsonObject jsonObject = read(stream);
                JsonArray jsonBeans = (JsonArray) jsonObject.get(BEANS);

                for (JsonValue jsonBean : jsonBeans) {
                    BeanDefinition definition = parse(jsonBean);
                    beanContainer.register(definition);
                }
            }
        } catch (RuntimeException e) {
            throw new BeanInstantiationException(e);
        }
    }

    protected JsonObject read(InputStream stream) {
        InputStreamReader isr = new InputStreamReader(stream);
        try {
            return JsonObject.readFrom(isr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected BeanDefinition parse(JsonValue jsonBean) {
        JsonObject jsBean = jsonBean.asObject();
        JsonValue tmp;

        String clazz = jsBean.get(CLASS).asString();
        String id = (tmp = jsBean.get(ID)) != null ? tmp.asString() : clazz;
        String scope = jsBean.get(SCOPE).asString();

        JsonArray empty = new JsonArray();
        JsonArray autowiredFieldsJson = (tmp = jsBean.get(AUTOWIRED)) != null ? tmp.asArray() : empty;
        JsonArray postConstructJson = (tmp = jsBean.get(INIT_METHODS)) != null ? tmp.asArray() : empty;
        JsonArray preDestroyJson = (tmp = jsBean.get(DESTROY_METHODS)) != null ? tmp.asArray() : empty;

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

        BeanDefinitionDescriptor descriptor = new BeanDefinitionDescriptor(id, clazz, scope,
                autowiredFieldNames, initMethodNames, destroyMethodNames);
        return beanDefinitionConverter.convert(descriptor);
    }
}
