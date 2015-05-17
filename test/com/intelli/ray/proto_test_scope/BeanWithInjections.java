package com.intelli.ray.proto_test_scope;

import com.intelli.ray.core.Inject;
import com.intelli.ray.core.ManagedComponent;

/**
 * Author: Sergey42
 * Date: 17.05.2015 21:36
 */
@ManagedComponent
public class BeanWithInjections {

    @Inject
    protected SingletonBean single;

    @Inject
    protected PrototypeBean prototypeBean;

    public SingletonBean getSingle() {
        return single;
    }

    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }
}
