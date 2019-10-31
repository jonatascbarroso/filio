package org.filio.filetransfer.service;

import java.io.InputStream;

import org.filio.filetransfer.entity.StoredObject;
import org.filio.filetransfer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the access to the main functionalities of the MinIO
 * Object Storage.
 */
@Service
@Slf4j
public class MinIOService {

    private final String SERVICE_NAME = "MinIO";

    @Value("${minio.serverName}")
    private String serverName;

    @Value("${minio.serverPort}")
    private int serverPort;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
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

    public void putObject(StoredObject object) {
        log.debug("Sending an object to MinIO");
        try {
            getMinioClient().putObject(bucketName,
                object.getId(), object.getContent(), object.getLength(),
                null, null, object.getContentType());
        } catch (Exception e) {
            log.error("An error has occurred trying to send an object to MinIO.", e);
            throw new ServiceException(SERVICE_NAME);
        }
    }

    public InputStream getObject(String id) {
        log.debug("Getting an object from MinIO");
        try {
            return getMinioClient().getObject(bucketName, id);
        } catch (Exception e) {
            log.error("An error has occurred trying to get a MinIO object.", e);
            throw new ServiceException(SERVICE_NAME);
        }
    }

}