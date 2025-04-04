package org.frosty.auth.annotation;

import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.frosty.auth.config.AuthConstant;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.auth.utils.TokenUtils;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class GetTokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 检查参数是否有 @GetAuth 注解
        return parameter.hasParameterAnnotation(GetToken.class);
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // 从请求头中获取 token 信息
        return CommonTokenLogic.extractToken(webRequest);
    }
}
