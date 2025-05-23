package org.frosty.sse_push.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
//import org.frosty.sse_push.handler.DynamicMapNameHelper;
//import org.frosty.sse_push.handler.DynamicTableNameType;
import org.frosty.sse_push.handler.JsonNodeTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MybatisPlusConfig {

//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        // 动态表名拦截器
//        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
//        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
//            // 获取参数方法
//            return DynamicTableNameType.getName(tableName, DynamicMapNameHelper.getRequestData());
//        });
//        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
//        return interceptor;
//    }

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