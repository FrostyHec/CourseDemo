package org.frosty.sse.config;

import org.frosty.sse.converter.SiteMessageConverter;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSEAutoConfiguration {

    @Bean
    public SiteMessageConverter messageDTOConverter() {
        return Mappers.getMapper(SiteMessageConverter.class);
    }
}
