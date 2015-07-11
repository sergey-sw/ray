package com.intelli.ray.base_scope;

import com.intelli.ray.meta.Inject;
import com.intelli.ray.meta.ManagedComponent;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 18:03
 */
@ManagedComponent
public class ParentBean {

    @Inject
    private BeanNoName beanNoName;

    public BeanNoName getBeanNoName() {
        return beanNoName;
    }
}
