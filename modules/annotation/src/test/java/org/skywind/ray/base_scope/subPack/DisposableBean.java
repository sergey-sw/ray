package org.skywind.ray.base_scope.subPack;

import org.skywind.ray.meta.ManagedComponent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 23:45
 */
@ManagedComponent
public class DisposableBean {

    Boolean resourcesReleased;

    public DisposableBean() {
    }

    public Boolean getResourcesReleased() {
        return resourcesReleased;
    }

    @PostConstruct
    protected void init() {
        resourcesReleased = false;
    }

    @PreDestroy
    protected void onDestroy() {
        resourcesReleased = true;
    }
}
