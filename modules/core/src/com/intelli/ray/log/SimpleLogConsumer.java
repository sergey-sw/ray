package com.intelli.ray.log;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 20:24
 */
@InterfaceAudience.Public
public abstract class SimpleLogConsumer implements LogConsumer {

    public abstract void log(String msg);

    @Override
    public void debug(String msg) {
        log(msg);
    }

    @Override
    public void info(String msg) {
        log(msg);
    }

    @Override
    public void warn(String msg) {
        log(msg);
    }

    @Override
    public void error(String msg) {
        log(msg);
    }
}
