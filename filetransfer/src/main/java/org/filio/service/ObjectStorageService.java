package org.filio.service;

import java.io.InputStream;

/**
 * The interface used to access a generic object-store.
 */
public interface ObjectStorageService {
    
    void putObject(InputStream content, String name);

    InputStream getObject(String name);

    void check(String name);

    String info(String name);

}