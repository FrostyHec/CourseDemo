package org.frosty.common_service.config;

import org.frosty.common_service.message_push.api.MessagePushService;
import org.frosty.common_service.message_push.api.impl.MessagePushServiceImpl;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.common_service.storage.api.impl.MockObjectStorageServiceImpl;
import org.frosty.common_service.storage.api.impl.ObjectStorageServiceImpl;
import org.frosty.infra.initalizer.MinioInitializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
@Configuration
@PropertySource("classpath:application.yaml")
public class CommonServiceAutoConfiguration {
//    @Value("${api.storage.type}")
    private String storageType="mock";

    @Bean
    public ObjectStorageService objectStorageService() {
        return switch (storageType) {
            case "mock" ->
                    new MockObjectStorageServiceImpl();
            case "remote" ->
                    new ObjectStorageServiceImpl();
            default ->
                    throw new IllegalArgumentException("storage type: "
                            + storageType + " not supported");
        };
    }

    @Bean
    public MessagePushService messagePushService() {
        return new MessagePushServiceImpl();
    }
}
