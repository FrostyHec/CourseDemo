package org.frosty.object_storage.service.cache.layer;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.cache.KVStorage;
import org.springframework.beans.factory.annotation.Value;
import org.frosty.common.exception.InternalException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EndOSSLayer implements KVStorage {
    private final MinioClient minioClient;

    @Value("${minio.bucket.serviceName}")
    private String bucketName;
    @Override
    public void put(String key, Object value) {// TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void putStream(String key, FlowWithMetadata in) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .stream(in.getStream(), -1, 10485760) // TODO 10MB check here
                            .contentType(in.getContentType())
                            .build()
            );
        } catch (Exception e){
            throw new InternalException("exception on push stream",e);
        }
    }

    @Override
    public Object get(String key) {// TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public FlowWithMetadata getStream(String key) {
        // TODO necessary to get content type?
        try {
            var flow = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build()
            );
            return new FlowWithMetadata(flow,null);
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new ExternalException(Response.getNotFound("no-found"));
            }
            throw new InternalException("unexpected error:",e);
        } catch (Exception e){
            throw new InternalException("unexpected error:",e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build()
            );
        }catch (Exception e){
            throw new InternalException("unexpected-error",e);
        }
    }

    @Override
    public boolean contains(String key) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new InternalException("unexpected error:",e);
        } catch (Exception e){
            throw new InternalException("unexpected error:",e);
        }
    }
}
