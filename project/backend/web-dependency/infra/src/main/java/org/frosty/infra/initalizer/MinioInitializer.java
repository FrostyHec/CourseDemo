package org.frosty.infra.initalizer;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import org.frosty.infra.config.minio.MinioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public class MinioInitializer implements Initializer{
    @Autowired
    private MinioConfig minioConfig;
    @Value("${minio.bucket.serviceName}")
    private String bucketName;
    @Override
    public void init() throws Exception {
        var minioClient = minioConfig.minioClient();
        // 清空桶内所有对象
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        for (Result<Item> result : results) {
            Item item = result.get();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(item.objectName()).build());
        }
    }
}
