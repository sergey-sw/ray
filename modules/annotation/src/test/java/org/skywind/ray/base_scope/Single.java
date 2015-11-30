package org.skywind.ray.base_scope;

import org.skywind.ray.meta.ManagedComponent;

/**
 * Author: Sergey42
 * Date: 16.11.13 17:07
 */
@ManagedComponent(name = "Single")
public class Single {

    protected AnnBeanNoName annBeanNoName;

    public AnnBeanNoName getAnnBeanNoName() {
        return annBeanNoName;
    }
}
