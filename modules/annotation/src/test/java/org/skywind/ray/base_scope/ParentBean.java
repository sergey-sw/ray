package org.skywind.ray.base_scope;

import org.skywind.ray.meta.Inject;
import org.skywind.ray.meta.ManagedComponent;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 08.07.2015 18:03
 */
@ManagedComponent
public class ParentBean {

    @Inject
    private AnnBeanNoName annBeanNoName;

    public AnnBeanNoName getAnnBeanNoName() {
        return annBeanNoName;
    }
}
