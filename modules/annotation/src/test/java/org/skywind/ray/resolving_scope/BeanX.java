package org.skywind.ray.resolving_scope;

import org.skywind.ray.meta.Inject;
import org.skywind.ray.meta.ManagedComponent;

/**
 * Author: Sergey Saiyan, sergey.sova42@gmail.com
 * Created on 01.12.15.
 */
@ManagedComponent
public class BeanX {

    @Inject
    private MyInt myInt;

    public MyInt getMyInt() {
        return myInt;
    }
}
