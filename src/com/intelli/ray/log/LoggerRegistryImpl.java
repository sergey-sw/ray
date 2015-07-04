package com.intelli.ray.log;

import com.intelli.ray.meta.InterfaceAudience;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:24
 */
@InterfaceAudience.Private
public class LoggerRegistryImpl implements LoggerRegistry {

    private List<LogConsumer> logConsumers = new CopyOnWriteArrayList<>();

    @Override
    public void addLogConsumer(LogConsumer logConsumer) {
        this.logConsumers.add(logConsumer);
    }

    @Override
    public void removeLogConsumer(LogConsumer logConsumer) {
        this.logConsumers.remove(logConsumer);
    }

    protected Iterable<LogConsumer> getLogConsumers() {
        return logConsumers;
    }
}
