package org.skywind.ray.base_scope;

import org.skywind.ray.meta.ManagedComponent;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 07.11.2015 19:49
 */
@ManagedComponent
public class MyInterfaceImpl implements MyInterface {
    @Override
    public void go() {
        System.out.println("ok");
    }
}
