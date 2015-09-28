package org.skywind.ray.resource;

import org.skywind.ray.meta.InterfaceAudience;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 15:48
 */
@InterfaceAudience.Development
public final class ResourceLoader {

    protected final Map<String, URLStreamHandler> handlers = new HashMap<>();

    public ResourceLoader() {
        handlers.put("file:", new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return u.openConnection();
            }
        });
        handlers.put("classpath:", new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                URL resourceUrl = classLoader.getResource(u.getPath());

                if (resourceUrl == null) {
                    return null;
                }

                return resourceUrl.openConnection();
            }
        });
    }

    public void registerHandler(String protocol, URLStreamHandler handler) {
        handlers.put(Objects.requireNonNull(protocol), Objects.requireNonNull(handler));
    }

    public InputStream open(String resourceLocation) {
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Resource location is null");
        }

        for (String protocol : handlers.keySet()) {
            if (resourceLocation.startsWith(protocol)) {
                try {
                    URL url = new URL(null, resourceLocation, handlers.get(protocol));
                    URLConnection urlConnection = url.openConnection();

                    if (urlConnection == null) {
                        return null;
                    }

                    return urlConnection.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }
}
