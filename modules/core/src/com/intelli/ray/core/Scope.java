package com.intelli.ray.core;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Author: Sergey42
 * Date: 16.11.13 17:02
 */
@InterfaceAudience.Public
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

    public static Scope fromId(String scopeStr) {
        if (scopeStr == null) {
            return null;
        }

        if (SINGLETON.id.equals(scopeStr)) {
            return SINGLETON;
        } else if (PROTOTYPE.id.equals(scopeStr)) {
            return PROTOTYPE;
        } else {
            throw new IllegalArgumentException("Unknown scope: " + scopeStr);
        }
    }

    public static Scope defaultScope(Scope src) {
        return src != null ? src : SINGLETON;
    }
}
