package com.intelli.ray.tests;


import com.intelli.ray.BeanWithPostConstruct1;
import com.intelli.ray.BeanWithPostConstruct2;
import com.intelli.ray.core.BeanContainer;
import com.intelli.ray.core.Context;
import com.intelli.ray.core.XmlContext;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 02.07.2015 23:26
 */
public class PostConstructOrderTest extends TestCase {

    public void test() throws Exception {
        Context context = new XmlContext("classpath:com/intelli/ray/context.xml");
        context.refresh();

        BeanContainer beanContainer = context.getBeanContainer();
        BeanWithPostConstruct2 bean = beanContainer.getBean(BeanWithPostConstruct2.class);

        assertEquals(1, bean.getField1());
        assertEquals(4, bean.getField2());
        assertEquals(1, bean.getInitInvokeCnt());
        assertEquals(1, bean.getInit1InvokeCnt());
        assertEquals(0, bean.getInit2InvokeCnt());

        BeanWithPostConstruct1 b1 = beanContainer.getBean(BeanWithPostConstruct1.class);
        assertEquals(b1.getField2(), 2);
        assertEquals(1, b1.getInit1InvokeCnt());
        assertEquals(1, b1.getInit2InvokeCnt());
    }
}
