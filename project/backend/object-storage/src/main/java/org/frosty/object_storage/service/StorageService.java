package org.frosty.object_storage.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final MinioClient minioClient;
    private final AccessKeyService accessKeyService;

    @Value("${minio.bucket.serviceName}")
    private String bucketName;

    public void uploadFile(String objectName, InputStream inputStream, String contentType) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, -1, 10485760) // TODO 10MB check here
                        .contentType(contentType)
                        .build()
        );
    }

    public InputStream getFile(String objectName) throws Exception {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new ExternalException(Response.getNotFound("no-found"));
            }
            throw e;
        }
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public boolean checkFileExist(String objectName) throws Exception {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw e;
        }
    }

    public void deleteAccessKey(String objectKey, String caseName) {
        accessKeyService.withdraw(objectKey, caseName);
    }

    public String getAccessKey(String objectKey, String caseName) {
        return accessKeyService.getOrCreate(objectKey, caseName);
    }
}
