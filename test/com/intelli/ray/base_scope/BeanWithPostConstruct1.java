package com.intelli.ray.base_scope;

import com.intelli.ray.core.ManagedComponent;

import javax.annotation.PostConstruct;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 23:24
 */
@ManagedComponent
public class BeanWithPostConstruct1 {

    protected int field1;
    protected int field2;


    @PostConstruct
    protected void init1() {
        field1 = 1;
    }

    @PostConstruct
    protected void init2() {
        field2 = 2;
    }

    public int getField1() {
        return field1;
    }

    public int getField2() {
        return field2;
    }
}
