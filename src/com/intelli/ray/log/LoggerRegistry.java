package com.intelli.ray.log;

import com.intelli.ray.meta.InterfaceAudience;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:21
 */
@InterfaceAudience.Public
public interface LoggerRegistry {

    void addLogConsumer(LogConsumer logConsumer);

    void removeLogConsumer(LogConsumer logConsumer);
}
