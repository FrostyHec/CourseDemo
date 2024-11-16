package org.frosty.infra.config;


import org.frosty.infra.config.minio.MinioConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfigAutoConfiguration {
    @Bean
    public MinioConfig minioConfig() {
        return new MinioConfig();
    }
}
