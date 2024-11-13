package org.frosty.sse.config;

import org.frosty.sse.converter.MessageDTOConverter;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthAutoConfiguration {

    @Bean
    public MessageDTOConverter messageDTOConverter() {
        return Mappers.getMapper(MessageDTOConverter.class);
    }
}
