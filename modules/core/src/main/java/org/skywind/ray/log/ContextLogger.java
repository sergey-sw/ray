package org.skywind.ray.log;

import org.skywind.ray.meta.InterfaceAudience;

import java.util.Date;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:29
 */
@InterfaceAudience.Development
public class ContextLogger {

    protected LoggerRegistryImpl loggerRegistry = new LoggerRegistryImpl();

    public LoggerRegistry getLoggerRegistry() {
        return loggerRegistry;
    }

    public void debug(String message) {
        for (LogConsumer consumer : loggerRegistry.getLogConsumers()) {
            consumer.debug(preProcess(message));
        }
    }

    public void info(String message) {
        for (LogConsumer consumer : loggerRegistry.getLogConsumers()) {
            consumer.info(preProcess(message));
        }
    }

    public void warn(String message) {
        for (LogConsumer consumer : loggerRegistry.getLogConsumers()) {
            consumer.warn(preProcess(message));
        }
    }

    public void error(String message) {
        for (LogConsumer consumer : loggerRegistry.getLogConsumers()) {
            consumer.error(preProcess(message));
        }
    }

    protected String preProcess(String msg) {
        return new Date() + " - " + msg;
    }
}
