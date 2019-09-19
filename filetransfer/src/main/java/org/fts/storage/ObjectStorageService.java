package org.fts.storage;

import java.io.InputStream;

/**
 * The interface used to access a generic object-store.
 */
public interface ObjectStorageService {
    
    void putObject(InputStream object);

    InputStream getObject(String id) throws ObjectNotFoundException;

    void check(String id) throws ObjectNotFoundException;

    String info(String id) throws ObjectNotFoundException;

}