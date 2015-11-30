package org.skywind.ray;


/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 23:24
 */
public class BeanWithPostConstruct1 {

    private int init1InvokeCnt;
    private int init2InvokeCnt;

    protected int field1;
    protected int field2;

    protected void init1() {
        init1InvokeCnt++;
        field1 = 1;
    }

    protected void init2() {
        init2InvokeCnt++;
        field2 = 2;
    }

    public int getField1() {
        return field1;
    }

    public int getField2() {
        return field2;
    }

    public int getInit1InvokeCnt() {
        return init1InvokeCnt;
    }

    public int getInit2InvokeCnt() {
        return init2InvokeCnt;
    }
}
