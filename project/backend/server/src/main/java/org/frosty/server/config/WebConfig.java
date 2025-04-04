package org.frosty.server.config;


import org.frosty.auth.annotation.GetTokenArgumentResolver;
import org.frosty.auth.annotation.GetTokenPassArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new GetTokenArgumentResolver());
        resolvers.add(new GetTokenPassArgumentResolver());
    }
}
