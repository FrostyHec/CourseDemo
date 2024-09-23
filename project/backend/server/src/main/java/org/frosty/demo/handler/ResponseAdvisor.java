package org.frosty.demo.handler;

import org.frosty.demo.response.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;

@NonNullApi
@ControllerAdvice
public class ResponseAdvisor implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class converterType) {
        return !returnType.getParameterType().equals(Response.class);
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        return Response.getSuccess(body);
    }
}

