package org.skywind.ray.log;

import org.skywind.ray.meta.InterfaceAudience;

/**
 * Interface for context logs consuming.
 * Debug level is used for minor events, like invoking lifecycle methods
 * or creating prototype instances.
 * Info level is used for base context events.
 * Warn level is used for minor problems, that occur during context initialization.
 * Error level is used to track an exception, before program control flow will be lost.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:12
 */
@InterfaceAudience.Public
public interface LogConsumer {

    void debug(String msg);

    void info(String msg);

    void warn(String msg);

    void error(String msg);
}
