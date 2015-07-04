package com.intelli.ray.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 04.07.2015 16:56
 */
public class Exceptions {

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
}
