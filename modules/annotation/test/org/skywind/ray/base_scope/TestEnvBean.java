package org.skywind.ray.base_scope;

import org.skywind.ray.meta.Inject;
import org.skywind.ray.meta.ManagedComponent;
import org.skywind.ray.meta.Profile;

import javax.annotation.PostConstruct;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 12.07.2015 16:52
 */
@Profile("test")
@ManagedComponent
public class TestEnvBean {

    int a1;
    int a2;

    @Inject
    protected TestEnvBean2 testEnvBean2;

    @Profile("integrationTest")
    @Inject
    protected TestEnvBean3 testEnvBean3;

    @Inject
    protected TestEnvBean3 teb3;

    @Inject
    protected Single single;

    @PostConstruct
    protected void initTest() {
        a1 = 1;
    }

    @Profile("integrationTest")
    @PostConstruct
    protected void initIntegrationTest() {
        a2 = 1;
    }

    public TestEnvBean2 getTestEnvBean2() {
        return testEnvBean2;
    }

    public TestEnvBean3 getTestEnvBean3() {
        return testEnvBean3;
    }

    public Single getSingle() {
        return single;
    }

    public int getA1() {
        return a1;
    }

    public int getA2() {
        return a2;
    }

    public TestEnvBean3 getTeb3() {
        return teb3;
    }
}
