package org.skywind.ray;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 14:19
 */
public class Bean3 {

    private Bean1 bean1;
    private Bean2 bean2;

    int init;
    int destroy;

    public Bean3() {
        init = 0;
        destroy = 0;
    }

    protected void init1() {
        init++;
    }

    protected void init2() {
        init++;
    }

    protected void destroy1() {
        destroy++;
    }

    protected void destroy2() {
        destroy++;
    }

    public Bean1 getBean1() {
        return bean1;
    }

    public Bean2 getBean2() {
        return bean2;
    }

    public int getInit() {
        return init;
    }

    public int getDestroy() {
        return destroy;
    }
}
