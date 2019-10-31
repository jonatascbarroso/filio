package org.filio.filetransfer.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.filio.filetransfer.entity.StoredObject;
import org.filio.filetransfer.exception.FileAccessException;
import org.filio.filetransfer.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * This class used to access a generic object-store.
 */
@Service
@Slf4j
public class ObjectStorageService {

    @Autowired
    private MinIOService minioService;

    @Autowired
    private MongoTemplate mongoTemplate;
    
    /**
     * Send an object.
     */
    public String putObject(MultipartFile file) {
        log.debug("Saving an object");
        String id = null;
        try {
            id = UUID.randomUUID().toString();
            InputStream content = file.getInputStream();
            String name = id + FilenameUtils.getExtension(file.getOriginalFilename());

            StoredObject object = new StoredObject();
            object.setId(id);
            object.setName(name);
            object.setCreatedTime(new Date());
            object.setContent(content);
            object.setContentType(file.getContentType());
            object.setLength(content.available());

            minioService.putObject(object);
            mongoTemplate.save(object);
        } catch (IOException e) {
            log.error("An error has occurred trying to access the file.", e);
            throw new FileAccessException();
        }
        return id;
    }

    /**
     * Get an object.
     */
    public StoredObject getObject(String id) {
        log.debug("Getting an object");
        StoredObject object = mongoTemplate.findById(id, StoredObject.class);
        object.setContent(minioService.getObject(id));
        return object;
    }

    /**
     * Check if an object exists.
     */
    public void check(String id) {
        log.debug("Checking if an object exists");
        StoredObject object = mongoTemplate.findById(id, StoredObject.class);
        if (object == null) {
            throw new ObjectNotFoundException(id);
        }
    }

    /**
     * Get the object metadata.
     */
    public StoredObject info(String id) {
        log.debug("Getting the object metadata");
        StoredObject object = mongoTemplate.findById(id, StoredObject.class);
        return object;
    }

}