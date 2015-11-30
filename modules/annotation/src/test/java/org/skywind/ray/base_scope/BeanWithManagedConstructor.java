package org.skywind.ray.base_scope;

import org.skywind.ray.core.Scope;
import org.skywind.ray.meta.ManagedComponent;
import org.skywind.ray.meta.ManagedConstructor;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 15:30
 */
@ManagedComponent(scope = Scope.PROTOTYPE)
public class BeanWithManagedConstructor {

    private Integer field;

    @ManagedConstructor
    public BeanWithManagedConstructor(int field) {
        this.field = field;
    }

    public int getField() {
        return field;
    }
}
