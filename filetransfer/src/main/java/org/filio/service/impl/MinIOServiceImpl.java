package org.filio.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.http.entity.ContentType;
import org.filio.exception.ObjectNotFoundException;
import org.filio.exception.ServiceException;
import org.filio.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Value;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the access to the main
 * functionalities of the MinIO Object Storage.
 */
@Slf4j
public class MinIOServiceImpl implements ObjectStorageService {

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

    private MinioClient getMinioClient() {
        log.debug("Creating a client to access the MinIO");
        try {
            MinioClient minioClient = new MinioClient(serverName, serverPort, accessKey, secretKey);
            boolean isBucketExist = minioClient.bucketExists(bucketName);
            if (isBucketExist) {
                log.debug("Bucket already exists; using it");
            } else {
                log.debug("Creating a bucket");
                minioClient.makeBucket(bucketName);
            }
            return minioClient;
        } catch (Exception e) {
            log.error("An error has occurred trying to use MinIO.", e);
            throw new ServiceException("MinIO");
        }
    }

    @Override
    public void putObject(InputStream content, String name) {
        log.debug("Sending an object to MinIO");
        ByteArrayInputStream object = new ByteArrayInputStream(content.toString().getBytes());

        getMinioClient().putObject(bucketName, name, content, size, null, null, ContentType.APPLICATION_OCTET_STREAM);
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
        ObjectStat stat = getMinioClient().statObject(bucketName, id);
        stat.contentType();
        stat.createdTime();
        stat.length();
        stat.name();
        return null;
    }
        
}