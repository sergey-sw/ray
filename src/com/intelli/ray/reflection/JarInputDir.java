package com.intelli.ray.reflection;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 21:59
 */
public class JarInputDir implements Scanner.Dir {
    private final URL url;
    JarInputStream jarInputStream;
    long cursor = 0;
    long nextCursor = 0;

    public JarInputDir(URL url) {
        this.url = url;
    }

    public String getPath() {
        return url.getPath();
    }

    public Iterable<Scanner.File> getFiles() {
        return new Iterable<Scanner.File>() {
            @Override
            public Iterator<Scanner.File> iterator() {
                return new Iterator<Scanner.File>() {

                    {
                        try {
                            jarInputStream = new JarInputStream(url.openConnection().getInputStream());
                        } catch (Exception e) {
                            throw new ReflectionException("Could not open url connection", e);
                        }
                    }

                    JarInputFile jarInputFile;

                    @Override
                    public boolean hasNext() {
                        while (true) {
                            try {
                                ZipEntry entry = jarInputStream.getNextJarEntry();
                                if (entry == null) {
                                    return false;
                                }

                                long size = entry.getSize();
                                if (size < 0) size = 0xffffffffl + size; //JDK-6916399
                                nextCursor += size;
                                if (!entry.isDirectory()) {
                                    jarInputFile = new JarInputFile(entry, JarInputDir.this, cursor, nextCursor);
                                    return true;
                                }
                            } catch (IOException e) {
                                throw new ReflectionException("could not get next zip entry", e);
                            }
                        }
                    }

                    @Override
                    public Scanner.File next() {
                        return jarInputFile;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Override
    public void close() {
        try {
            if (jarInputStream != null) {
                jarInputStream.close();
            }
        } catch (IOException ignored) {
        }
    }
}

