package org.frosty.site_dispatch.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import org.frosty.site_dispatch.handler.JsonNodeTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO May useless?
@Configuration
@MapperScan("org.frosty.site_dispatch.mapper")
public class MyBatisPlusConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(MybatisConfiguration configuration) {
                configuration.getTypeHandlerRegistry().register(JsonNode.class, JsonNodeTypeHandler.class);
            }
        };
    }
}
