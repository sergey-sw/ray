package org.skywind.ray.util;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 17.07.2015 18:06
 */
public final class IO {

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
