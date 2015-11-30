package org.skywind.ray.tests;

import junit.framework.TestCase;
import org.skywind.ray.core.BeanDefinition;
import org.skywind.ray.core.Context;
import org.skywind.ray.core.InternalBeanContainer;
import org.skywind.ray.core.Scope;
import org.skywind.ray.log.LogConsumer;
import org.skywind.ray.log.LoggerRegistry;
import org.skywind.ray.log.SimpleLogConsumer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 18:45
 */
public class LoggingTest extends TestCase {

    public void testLogging() throws Exception {
        Context context = new TestAnnotationContext();
        LoggerRegistry loggerRegistry = context.getLoggerRegistry();

        final StringBuilder sb = new StringBuilder();
        LogConsumer consumer = new SimpleLogConsumer() {
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
        beanContainer.register(new BeanDefinition("String-098765", Scope.SINGLETON, String.class, "098765",
                new Method[0], new Method[0], new Field[0]));

        String log2 = sb.toString();
        assertEquals(log, log2);
    }
}
