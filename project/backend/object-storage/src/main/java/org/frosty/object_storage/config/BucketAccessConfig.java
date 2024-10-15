package org.frosty.object_storage.config;

import io.minio.MinioClient;
import org.frosty.infra.config.minio.MinioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucketAccessConfig {
    @Autowired
    private MinioConfig minioConfig;
    @Bean
    public MinioClient minioClient(){
        return minioConfig.minioClient();
    }
}
