package com.intelli.ray.tests;

import com.intelli.ray.core.*;
import com.intelli.ray.log.LogConsumer;
import com.intelli.ray.log.LoggerRegistry;
import junit.framework.TestCase;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:45
 */
public class LoggingTest extends TestCase {

    public void testLogging() throws Exception {
        Context context = new AnnotationBasedContext("com.intelli.ray.base_scope");
        LoggerRegistry loggerRegistry = context.getLoggerRegistry();

        final StringBuilder sb = new StringBuilder();
        LogConsumer consumer = new LogConsumer() {
            @Override
            public void log(String msg) {
                sb.append(msg).append("\n");
            }
        };
        loggerRegistry.addLogConsumer(consumer);
        context.refresh();

        String log = sb.toString();
        assertNotNull(log);
        System.out.println(log);

        loggerRegistry.removeLogConsumer(consumer);

        InternalBeanContainer beanContainer = (InternalBeanContainer) context.getBeanContainer();
        beanContainer.register(new BeanDefinition("String-098765", Scope.SINGLETON, String.class, "098765", null, null));

        String log2 = sb.toString();
        assertEquals(log, log2);
    }
}
