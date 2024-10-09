package org.frosty.common.config;


import org.frosty.common.handler.ExceptionAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultExceptionAdvisorConfig {
    @Bean
    public ExceptionAdvisor exceptionAdvisor() {
        return new ExceptionAdvisor();
    }
}
