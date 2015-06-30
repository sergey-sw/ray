package com.intelli.ray.reflection;

import java.io.File;
import java.util.*;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 21:36
 */
public class SystemDir implements Scanner.Dir {

    private final File file;

    public SystemDir(File file) {
        if (file != null && (!file.isDirectory() || !file.canRead())) {
            throw new RuntimeException("Cannot use dir " + file);
        }

        this.file = file;
    }

    @Override
    public String getPath() {
        if (file == null) {
            return "/NO-SUCH-DIRECTORY/";
        }
        return file.getPath().replace("\\", "/");
    }

    @Override
    public Iterable<Scanner.File> getFiles() {
        if (file == null || !file.exists()) {
            return Collections.emptyList();
        }
        return new Iterable<Scanner.File>() {
            @Override
            public Iterator<Scanner.File> iterator() {
                return new Iterator<Scanner.File>() {

                    final LinkedList<File> queue = new LinkedList<>();

                    {
                        queue.addAll(listFiles(file));
                    }

                    @Override
                    public boolean hasNext() {
                        if (queue.isEmpty()) return false;

                        if (queue.peek().isDirectory()) {
                            File head = queue.poll();
                            queue.addAll(listFiles(head));
                            return hasNext();
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public Scanner.File next() {
                        return new SystemFile(SystemDir.this, queue.poll());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private static List<File> listFiles(final File file) {
        File[] files = file.listFiles();

        if (files != null)
            return new ArrayList<>(Arrays.asList(files));
        else
            return new ArrayList<>();
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return getPath();
    }
}
