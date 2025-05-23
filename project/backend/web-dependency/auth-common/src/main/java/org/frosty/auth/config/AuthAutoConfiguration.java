package org.frosty.auth.config;

import org.frosty.auth.utils.JwtHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthAutoConfiguration {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    @Bean
    public JwtHandler jwtHandler() {
        return new JwtHandler(secret, expiration);
    }
}
