package org.skywind.ray.reflection;

import org.skywind.ray.meta.InterfaceAudience;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 21:55
 */
@InterfaceAudience.Private
public class ZipFile implements Scanner.File {
    private final ZipDir root;
    private final ZipEntry entry;

    public ZipFile(final ZipDir root, ZipEntry entry) {
        this.root = root;
        this.entry = entry;
    }

    public String getName() {
        String name = entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }

    public String getRelativePath() {
        return entry.getName();
    }

    public InputStream openInputStream() throws IOException {
        return root.jarFile.getInputStream(entry);
    }

    @Override
    public String toString() {
        return root.getPath() + "!" + java.io.File.separatorChar + entry.toString();
    }
}

