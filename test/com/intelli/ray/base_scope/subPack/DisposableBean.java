package com.intelli.ray.base_scope.subPack;

import com.intelli.ray.core.ManagedComponent;

import javax.annotation.PreDestroy;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 23:45
 */
@ManagedComponent
public class DisposableBean {

    Boolean resourcesReleased;

    public DisposableBean() {
        resourcesReleased = false;
    }

    public Boolean getResourcesReleased() {
        return resourcesReleased;
    }

    @PreDestroy
    public void onDestroy() {
        resourcesReleased = true;
    }
}
