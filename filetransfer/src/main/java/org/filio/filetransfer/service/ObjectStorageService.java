package org.filio.filetransfer.service;

import java.io.InputStream;

/**
 * The interface used to access a generic object-store.
 */
public interface ObjectStorageService {
    
    /**
     * Send an object.
     */
    public abstract void putObject(String id, InputStream content);

    /**
     * Get an object.
     */
    public abstract InputStream getObject(String id);

    /**
     * Check if an object exists.
     */
    public abstract void check(String id);

    /**
     * Get the object info.
     */
    public abstract String info(String id);

}