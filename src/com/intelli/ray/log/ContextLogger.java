package com.intelli.ray.log;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:29
 */
public class ContextLogger {

    protected LoggerRegistryImpl loggerRegistry = new LoggerRegistryImpl();

    public LoggerRegistry getLoggerRegistry() {
        return loggerRegistry;
    }

    public void log(String message) {
        for (LogConsumer consumer : loggerRegistry.getLogConsumers()) {
            consumer.log(message);
        }
    }
}
