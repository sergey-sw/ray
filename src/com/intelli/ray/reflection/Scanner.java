package com.intelli.ray.reflection;

import com.intelli.ray.log.ContextLogger;
import com.intelli.ray.util.Exceptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarFile;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 21:02
 */
public class Scanner {

    private static List<UrlType> defaultUrlTypes = Arrays.<UrlType>asList(DefaultUrlTypes.values());

    private ContextLogger logger;
    private AnyFilter filter;
    private Set<Class> classes = new HashSet<>();

    public Scanner(ContextLogger logger, String... locations) {
        this.logger = logger;
        this.filter = new AnyFilter(locations);

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Set<URL> allUrls = new HashSet<>();
        for (String location : locations) {
            allUrls.addAll(forPackage(location, cl));
        }

        for (URL url : allUrls) {
            scan(url);
        }
    }

    public Set<Class> getClasses() {
        return classes;
    }

    private void scan(URL url) {
        for (final File file : fromURL(url).getFiles()) {
            String input = file.getRelativePath().replace('/', '.');
            if (filter.check(input)) {
                if (input.endsWith(".class")) {
                    String name = null;
                    try {
                        name = input.substring(0, input.lastIndexOf(".class"));
                        classes.add(Class.forName(name));
                    } catch (ClassNotFoundException e) {
                        logger.warn("Could not load class " + name + "\n" + Exceptions.toStr(e));
                    }
                }
            }
        }
    }

    private Collection<URL> forPackage(String name, ClassLoader classLoader) {
        return forResource(resourceName(name), classLoader);
    }

    private Collection<URL> forResource(String resourceName, ClassLoader classLoader) {
        final List<URL> result = new ArrayList<>();
        try {
            final Enumeration<URL> urls = classLoader.getResources(resourceName);
            while (urls.hasMoreElements()) {
                final URL url = urls.nextElement();
                int index = url.toExternalForm().lastIndexOf(resourceName);
                if (index != -1) {
                    result.add(new URL(url.toExternalForm().substring(0, index)));
                } else {
                    result.add(url); //whatever
                }
            }
        } catch (IOException e) {
            logger.warn("Error getting resources for " + resourceName + "\n" + Exceptions.toStr(e));
        }
        return distinctUrls(result);
    }

    private static Collection<URL> distinctUrls(Collection<URL> urls) {
        try {
            Set<URI> uris = new HashSet<>(urls.size());
            for (URL url : urls) {
                uris.add(url.toURI());
            }
            List<URL> result = new ArrayList<>(uris.size());
            for (URI uri : uris) {
                result.add(uri.toURL());
            }
            return result;
        } catch (Exception e) {
            //return original urls as set, prefer backward comp over potential performance issue
            return new HashSet<>(urls);
        }
    }

    private static String resourceName(String name) {
        if (name != null) {
            String resourceName = name.replace(".", "/");
            resourceName = resourceName.replace("\\", "/");
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1);
            }
            return resourceName;
        }
        return null;
    }

    private Dir fromURL(final URL url) {
        return fromURL(url, defaultUrlTypes);
    }

    /**
     * tries to create a Dir from the given url, using the given urlTypes
     */
    private Dir fromURL(final URL url, final List<UrlType> urlTypes) {
        for (UrlType type : urlTypes) {
            try {
                if (type.matches(url)) {
                    Dir dir = type.createDir(url);
                    if (dir != null) return dir;
                }
            } catch (Throwable e) {
                logger.warn("Could not create Dir using " + type + " from url " + url.toExternalForm() + ". " +
                        "Skipping.\n" + Exceptions.toStr(e));
            }
        }

        throw new RuntimeException("Could not create Dir from url, no matching UrlType was found");
    }

    private static java.io.File getFile(URL url) {
        java.io.File file;
        String path;

        try {
            path = url.toURI().getSchemeSpecificPart();
            if ((file = new java.io.File(path)).exists()) return file;
        } catch (URISyntaxException ignored) {
        }

        try {
            path = URLDecoder.decode(url.getPath(), "UTF-8");
            if (path.contains(".jar!")) path = path.substring(0, path.lastIndexOf(".jar!") + ".jar".length());
            if ((file = new java.io.File(path)).exists()) return file;

        } catch (UnsupportedEncodingException ignored) {
        }

        try {
            path = url.toExternalForm();
            if (path.startsWith("jar:")) path = path.substring("jar:".length());
            if (path.startsWith("wsjar:")) path = path.substring("wsjar:".length());
            if (path.startsWith("file:")) path = path.substring("file:".length());
            if (path.contains(".jar!")) path = path.substring(0, path.indexOf(".jar!") + ".jar".length());
            if ((file = new java.io.File(path)).exists()) return file;

            path = path.replace("%20", " ");
            if ((file = new java.io.File(path)).exists()) return file;

        } catch (Exception ignored) {
        }

        return null;
    }


    protected static interface Dir {
        String getPath();

        Iterable<File> getFiles();

        void close();
    }

    protected static interface File {
        String getName();

        String getRelativePath();

        InputStream openInputStream() throws IOException;
    }

    interface UrlType {
        boolean matches(URL url) throws Exception;

        Dir createDir(URL url) throws Exception;
    }

    protected static enum DefaultUrlTypes implements UrlType {
        JAR_FILE {
            @Override
            public boolean matches(URL url) {
                return url.getProtocol().equals("file") && url.toExternalForm().contains(".jar");
            }

            @Override
            public Dir createDir(final URL url) throws Exception {
                return new ZipDir(new JarFile(getFile(url)));
            }
        },

        DIRECTORY {
            @Override
            public boolean matches(URL url) {
                return url.getProtocol().equals("file") && !url.toExternalForm().contains(".jar") &&
                        getFile(url).isDirectory();
            }

            @Override
            public Dir createDir(final URL url) throws Exception {
                return new SystemDir(getFile(url));
            }
        },

        JAR_INPUT_STREAM {
            @Override
            public boolean matches(URL url) throws Exception {
                return url.toExternalForm().contains(".jar");
            }

            @Override
            public Dir createDir(final URL url) throws Exception {
                return new JarInputDir(url);
            }
        }
    }
}
