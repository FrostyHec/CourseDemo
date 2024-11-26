package org.frosty.server.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.entity.bo.market.action_type.ActionParam;
import org.frosty.server.entity.handler.JsonNodeTypeHandler;
import org.frosty.server.utils.ActionParamTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); // 添加分页拦截器
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry().register(ActionParam.class, new ActionParamTypeHandler());
        configuration.getTypeHandlerRegistry().register(JsonNode.class, new JsonNodeTypeHandler());
            configuration.getTypeHandlerRegistry().register(LangchainController.ChatContext.class, new ChatContextTypeHandler());
        };
    }
}