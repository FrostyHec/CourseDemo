package org.frosty.demo.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.frosty.demo.exception.ExternalException;
import org.frosty.demo.utils.JwtUtil;
import org.frosty.demo.response.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;




@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    // TODO 处理no-auth的情况并且传给下层
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        log.info("auth verifying request :{}",request);
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")){
            throwUnauthorizedException("no-auth");
        }
        token = token.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                throwUnauthorizedException("auth-expired");
            }
        }catch (Exception e){
            log.warn("auth-invalid:"+token,e);
            throwUnauthorizedException("invalid-auth");
        }
        Long userId = jwtUtil.getSubject(token);
        request.setAttribute("request_user_id", userId);
        return true;
    }

    private void throwUnauthorizedException(String msg) {
        log.warn("auth verification failed");
        throw new ExternalException(Response.getUnauthorized(msg));
    }
}
