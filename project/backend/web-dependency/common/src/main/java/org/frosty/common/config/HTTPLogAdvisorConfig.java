package org.frosty.common.config;

import jakarta.servlet.*;
import org.frosty.common.annotation.DefaultHTTPLogAdvisor;
import org.frosty.common.constant.AdvisorConstant;
import org.frosty.common.handler.HTTPLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Map;

@Configuration
public class HTTPLogAdvisorConfig implements ImportAware, WebMvcConfigurer {

    private boolean onTestLog = false;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> attributes = importMetadata.getAnnotationAttributes(DefaultHTTPLogAdvisor.class.getName());
        assert attributes != null;
        this.onTestLog = (Boolean) attributes.get("value");
    }

    @Bean
    public FilterRegistrationBean<Filter> loggingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        if (onTestLog) {
            registrationBean.setFilter(new HTTPLogFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(AdvisorConstant.defaultServletFilterOrder);
        }else{
            registrationBean.setFilter((servletRequest, servletResponse, filterChain) -> {});
        }
        return registrationBean;
    }
}
