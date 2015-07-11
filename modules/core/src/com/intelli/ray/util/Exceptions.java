package com.intelli.ray.util;

import com.intelli.ray.meta.InterfaceAudience;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 16:56
 */
@InterfaceAudience.Development
public final class Exceptions {

    private static final byte[] NEW_LINE;

    static {
        try {
            NEW_LINE = "\n".getBytes("UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toStr(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(baos));
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }

    public static String toStr(String title, Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(title.getBytes("UTF-8"));
            baos.write(NEW_LINE);
            t.printStackTrace(new PrintStream(baos));
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }
}
