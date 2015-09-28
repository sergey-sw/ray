package org.skywind.ray.reflection;

import org.skywind.ray.meta.InterfaceAudience;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 21:51
 */
@InterfaceAudience.Private
public class ZipDir implements Scanner.Dir {

    final java.util.zip.ZipFile jarFile;

    public ZipDir(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    @Override
    public String getPath() {
        return jarFile.getName();
    }

    @Override
    public Iterable<Scanner.File> getFiles() {
        return new Iterable<Scanner.File>() {
            public Iterator<Scanner.File> iterator() {
                return new Iterator<Scanner.File>() {

                    final Enumeration<? extends ZipEntry> entries = jarFile.entries();
                    ZipEntry current;

                    @Override
                    public boolean hasNext() {
                        if (entries.hasMoreElements()) {
                            current = entries.nextElement();
                            if (current.isDirectory()) {
                                return hasNext();
                            }
                        }
                        return false;
                    }

                    @Override
                    public Scanner.File next() {
                        return new ZipFile(ZipDir.this, current);
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
            jarFile.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public String toString() {
        return jarFile.getName();
    }
}
