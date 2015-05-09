package com.intelli.ray.core;

/**
 * Author: Sergey42
 * Date: 16.11.13 17:02
 */
public enum Scope {
    SINGLETON("singleton"),
    PROTOTYPE("prototype");

    private String id;

    Scope(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Scope fromId(String fromId) {
        if (SINGLETON.id.equals(fromId)) {
            return SINGLETON;
        } else if (PROTOTYPE.id.equals(fromId)) {
            return PROTOTYPE;
        }
        return null;
    }
}
