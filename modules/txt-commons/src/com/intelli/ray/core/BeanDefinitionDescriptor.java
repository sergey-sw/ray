package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 12:46
 */
@InterfaceAudience.Development
public class BeanDefinitionDescriptor {

    public static final String BEAN = "bean";
    public static final String BEANS = "beans";

    public static final String PROFILES = "profiles";
    public static final String PROFILE = "profile";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CLASS = "class";
    public static final String SCOPE = "scope";
    public static final String INIT_METHODS = "init-methods";
    public static final String DESTROY_METHODS = "destroy-methods";
    public static final String AUTOWIRED = "autowired";
    public static final String ACTIVE = "active";

    public final String id;
    public final String clazz;
    public final String scope;
    public final Iterable<String> initMethods;
    public final Iterable<String> destroyMethods;
    public final Iterable<String> autowired;
    public final Collection<String> profiles;

    public BeanDefinitionDescriptor(String id, String clazz, String scope,
                                    String autowired, String initMethods, String destroyMethods,
                                    String profiles) {
        this.profiles = split(profiles);
        this.autowired = split(autowired);
        this.destroyMethods = split(destroyMethods);
        this.initMethods = split(initMethods);
        this.scope = scope;
        this.clazz = Objects.requireNonNull(clazz);
        this.id = id;
    }

    public BeanDefinitionDescriptor(String id, String clazz, String scope,
                                    Iterable<String> autowired,
                                    Iterable<String> initMethods,
                                    Iterable<String> destroyMethods,
                                    Collection<String> profiles) {
        this.autowired = autowired;
        this.destroyMethods = destroyMethods;
        this.initMethods = initMethods;
        this.scope = scope;
        this.profiles = profiles;
        this.clazz = Objects.requireNonNull(clazz);
        this.id = id;
    }

    private Collection<String> split(String src) {
        return src != null ? Arrays.asList(src.split(",")) : null;
    }
}
