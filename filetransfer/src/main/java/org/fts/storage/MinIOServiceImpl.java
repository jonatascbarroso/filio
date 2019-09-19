package org.fts.storage;

import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the access to the main
 * functionalities of the MinIO Object Storage.
 */
@Slf4j
public class MinIOServiceImpl implements ObjectStorageService {

    @Override
    public void putObject(InputStream object) {
        log.debug("Sending an object to MinIO");
        // TODO
    }

    @Override
    public InputStream getObject(String id) throws ObjectNotFoundException {
        log.debug("Getting an object from MinIO");
        // TODO
        return null;
    }

    @Override
    public void check(String id) throws ObjectNotFoundException {
        log.debug("Checking if an object exists on MinIO");
        // TODO
    }

    @Override
    public String info(String id) throws ObjectNotFoundException {
        log.debug("Getting an object info from MinIO");
        // TODO
        return null;
    }
        
}