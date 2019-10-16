package org.filio.service.impl;

import java.io.InputStream;

import org.apache.http.entity.ContentType;
import org.filio.entity.StoredObject;
import org.filio.exception.ObjectNotFoundException;
import org.filio.exception.ServiceException;
import org.filio.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the access to the main functionalities of the MinIO
 * Object Storage.
 */
@Service
@Slf4j
public class MinIOServiceImpl implements ObjectStorageService {

    private final String SERVICE_NAME = "MinIO";

    @Value("${minio.serverName}")
    private String serverName;

    @Value("${minio.serverPort}")
    private int serverPort;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucketName;

    private MinioClient minioClient;

    private Boolean isBucketExist;

    private void createBucket() {
        try {
            isBucketExist = minioClient.bucketExists(bucketName);
            if (isBucketExist) {
                log.debug("Bucket already exists; using it");
            } else {
                log.debug("Creating a bucket");
                minioClient.makeBucket(bucketName);
            }
        } catch (Exception e) {
            log.error("An error has occurred trying to create or use a MinIO bucket.", e);
            throw new ServiceException(SERVICE_NAME);
        }
    }

    private MinioClient getMinioClient() {
        log.debug("Creating a client to access the MinIO");
        if (minioClient == null) {
            try {
                minioClient = new MinioClient(serverName, serverPort, accessKey, secretKey);
                if (isBucketExist == null || Boolean.FALSE.equals(isBucketExist)) {
                    createBucket();
                }
            } catch (Exception e) {
                log.error("An error has occurred trying to create a MinIO client.", e);
                throw new ServiceException(SERVICE_NAME);
            }
        }
        return minioClient;
    }

    @Override
    public String putObject(String id, InputStream content) {
        log.debug("Sending an object to MinIO");
        try {
            Long size = Long.valueOf(content.available());
            getMinioClient().putObject(bucketName, id, content, size, null, null, ContentType.APPLICATION_OCTET_STREAM.toString());
            return id;
        } catch (Exception e) {
            log.error("An error has occurred trying to send an object to MinIO.", e);
            throw new ServiceException(SERVICE_NAME);
        }
    }

    @Override
    public InputStream getObject(String id) throws ObjectNotFoundException {
        log.debug("Getting an object from MinIO");
        try {
            InputStream content = getMinioClient().getObject(bucketName, id);
            return content;
        } catch (Exception e) {
            log.error("An error has occurred trying to get a MinIO object.", e);
            throw new ServiceException(SERVICE_NAME);
        }
    }

    @Override
    public void check(String id) throws ObjectNotFoundException {
        log.debug("Checking if an object exists on MinIO");
        try {
            ObjectStat stat = getMinioClient().statObject(bucketName, id);
            if (stat == null) {
                throw new ObjectNotFoundException(id);
            }
        } catch (Exception e) {
            log.error("An error has occurred trying to check if a MinIO object exists.", e);
            throw new ServiceException(SERVICE_NAME);
        }
    }

    @Override
    public String info(String id) throws ObjectNotFoundException {
        log.debug("Getting the object info from MinIO, but without content");
        StoredObject storedObject = new StoredObject();
        try {
            ObjectStat stat = getMinioClient().statObject(bucketName, id);
            storedObject.setId(stat.name());
            storedObject.setCreatedTime(stat.createdTime());
            storedObject.setLength(stat.length());
        } catch (Exception e) {
            log.error("An error has occurred trying to get a Minio object info.", e);
            throw new ServiceException(SERVICE_NAME);
        }
        return storedObject.json();
    }

}