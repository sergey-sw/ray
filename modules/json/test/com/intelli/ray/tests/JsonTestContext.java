package com.intelli.ray.tests;

import com.intelli.ray.core.JsonContext;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 16:20
 */
public class JsonTestContext extends JsonContext {

    public JsonTestContext() {
        super("classpath:com/intelli/ray/tests/context.json");
    }
}
