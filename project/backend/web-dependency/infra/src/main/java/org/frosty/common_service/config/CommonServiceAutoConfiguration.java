package org.frosty.common_service.config;

import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.common_service.im.api.impl.MessagePushServiceImpl;
import org.frosty.common_service.im.api.impl.MockMessagePushServiceImpl;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.common_service.storage.api.SharedBiMapService;
import org.frosty.common_service.storage.api.impl.MockObjectStorageServiceImpl;
import org.frosty.common_service.storage.api.impl.MockSharedBiMapServiceImpl;
import org.frosty.common_service.storage.api.impl.ObjectStorageServiceImpl;
import org.frosty.common_service.storage.api.impl.SharedBiMapServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonServiceAutoConfiguration {
    @Value("${api.storage.type}")
    private String storageType;

    @Value("${api.message.type}")
    private String messageType;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectStorageService objectStorageService() {
        return switch (storageType) {
            case "mock" -> new MockObjectStorageServiceImpl();
            case "remote" -> new ObjectStorageServiceImpl();
            default -> throw new IllegalArgumentException("storage type: "
                    + storageType + " not supported");
        };
    }
    @Bean
    public SharedBiMapService sharedBiMapService() {
        return switch (storageType) {
            case "mock" -> new MockSharedBiMapServiceImpl();
            case "remote" -> new SharedBiMapServiceImpl();
            default -> throw new IllegalArgumentException("storage type: "
                    + storageType + " not supported");
        };
    }

    @Bean
    public MessagePushService messagePushService() {
        return switch (messageType) {
            case "mock" -> new MockMessagePushServiceImpl();
            case "remote" -> new MessagePushServiceImpl();
            default -> throw new IllegalArgumentException("message type: "
                    + messageType + " not supported");
        };
    }
}
