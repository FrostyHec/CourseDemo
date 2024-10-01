package org.forsty.auth.annotation;

import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.forsty.auth.utils.TokenUtils;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
@Slf4j
public class GetAuthArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 检查参数是否有 @GetAuth 注解
        return parameter.hasParameterAnnotation(GetAuth.class);
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // 从请求头中获取 token 信息
        String token = webRequest.getHeader("X-Forwarded-User");
        try {
            return TokenUtils.tokenInfoFromString(token);
        } catch (Exception e) {
            log.warn("X-Forwarded-User invalid:{}", token, e);
            throw new ExternalException(Response.getBadRequest("X-Forwarded-User invalid:" + token));
        }
    }
}
