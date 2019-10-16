package org.filio.service;

import java.io.InputStream;

/**
 * The interface used to access a generic object-store.
 */
public interface ObjectStorageService {
    
    /**
     * Send an object.
     */
    String putObject(InputStream content);

    /**
     * Get an object.
     */
    InputStream getObject(String id);

    /**
     * Check if an object exists.
     */
    void check(String id);

    /**
     * Get the object info.
     */
    String info(String id);

}