package com.intelli.ray.base_scope;

import com.intelli.ray.meta.Inject;
import com.intelli.ray.meta.ManagedComponent;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 18:05
 */
@ManagedComponent
public class ChildBean extends ParentBean {

    @Inject
    protected Single single;

    public Single getSingle() {
        return single;
    }
}
