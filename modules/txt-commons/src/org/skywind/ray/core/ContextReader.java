package org.skywind.ray.core;

import org.skywind.ray.meta.InterfaceAudience;

import java.io.InputStream;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 11.07.2015 12:42
 */
@InterfaceAudience.Development
public interface ContextReader {

    /**
     * Reads the information from stream and returns ContextDescriptor
     *
     * @param inputStream input stream with context descriptor
     * @return context descriptor
     * @throws ContextReader.ReaderException if context definition is invalid
     */
    ContextDescriptor readContext(InputStream inputStream) throws ReaderException;

    class ReaderException extends RuntimeException {
        public ReaderException(Throwable cause) {
            super(cause);
        }
    }
}
