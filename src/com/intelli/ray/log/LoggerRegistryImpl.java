package com.intelli.ray.log;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:24
 */
public class LoggerRegistryImpl implements LoggerRegistry {

    private List<LogConsumer> logConsumers = new ArrayList<>();

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
