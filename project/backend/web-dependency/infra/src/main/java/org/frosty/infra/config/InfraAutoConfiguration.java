package org.frosty.infra.config;

import org.frosty.infra.initalizer.DatabaseInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class InfraAutoConfiguration {
    @Bean
    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer();
    }
}