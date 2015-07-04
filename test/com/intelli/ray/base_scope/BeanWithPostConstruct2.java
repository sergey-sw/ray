package com.intelli.ray.base_scope;

import com.intelli.ray.meta.ManagedComponent;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 23:25
 */
@ManagedComponent
public class BeanWithPostConstruct2 extends BeanWithPostConstruct1 {

    protected void init2() {
        field2 = 4;
    }
}
