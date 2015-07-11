package com.intelli.ray.base_scope;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 16:35
 */
public class LifecycleBean {

    Boolean resourcesReleased;

    public LifecycleBean() {
    }

    public Boolean getResourcesReleased() {
        return resourcesReleased;
    }

    public void init() {
        resourcesReleased = false;
    }

    public void onDestroy() {
        resourcesReleased = true;
    }
}
