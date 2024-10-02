package org.frosty.common.config;


import org.frosty.common.handler.ResponseAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {
    @Bean
    public ResponseAdvisor responseAdvisor() {
        return new ResponseAdvisor();
    }
}
