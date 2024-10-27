package org.frosty.test_common.config;

import org.frosty.infra.initalizer.DatabaseInitializer;
import org.frosty.test_common.utils.JsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestCommonAutoConfiguration {
    @Bean
    public JsonUtils jsonUtils() {
        return new JsonUtils();
    }
}