package com.intelli.ray.base_scope.subPack;

import com.intelli.ray.core.Disposable;
import com.intelli.ray.core.ManagedComponent;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 23:45
 */
@ManagedComponent
public class DisposableBean implements Disposable {

    Boolean resourcesReleased;

    public DisposableBean() {
        resourcesReleased = false;
    }

    public Boolean getResourcesReleased() {
        return resourcesReleased;
    }

    @Override
    public void onDestroy() {
        resourcesReleased = true;
    }
}
