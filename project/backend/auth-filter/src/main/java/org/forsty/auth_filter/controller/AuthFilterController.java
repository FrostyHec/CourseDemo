package org.forsty.auth_filter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.forsty.auth.config.AuthConstant;
import org.forsty.auth.entity.AuthInfo;
import org.forsty.auth.entity.TokenInfo;
import org.forsty.auth.exception.InvalidTokenException;
import org.forsty.auth.utils.JwtHandler;
import org.forsty.auth.utils.TokenUtils;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(PathConstant.INTERNAL_API + "/auth")
@RequiredArgsConstructor
public class AuthFilterController {
    private final JwtHandler jwtHandler;
    private final ObjectMapper objectMapper;

    @RequestMapping
    public void auth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        // null is acceptable for api not requiring auth
        if (token == null) {
            setAuthorizedResponse(TokenInfo.getWithoutAuthInfo(), response);
            return;
        }
        // check if token is valid
        Claims claims;
        try {
            if (!token.startsWith("Bearer ")) {
                throw new InvalidTokenException("token with incorrect format", null);
            }
            claims = jwtHandler.getClaimsFromToken(token.substring(7));
        } catch (InvalidTokenException e) {
            setUnauthorizedResponse("invalid-auth", response);
            return;
        }
        // check if token is expired
        if (jwtHandler.isTokenExpired(claims)) {
            setUnauthorizedResponse("auth-expired", response);
            return;
        }
        AuthInfo authInfo = jwtHandler.getToken(claims);
        setAuthorizedResponse(TokenInfo.getWithAuthInfo(authInfo), response);
    }

    private void setAuthorizedResponse(TokenInfo tokenInfo, HttpServletResponse response) {
        response.setHeader(AuthConstant.parsedHeader, TokenUtils.tokenInfoToString(tokenInfo));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void setUnauthorizedResponse(String respInfo, HttpServletResponse response) {
        try {
            response.setStatus(AuthConstant.internalUnauthorizedCode);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), Response.getUnauthorized(respInfo));
        } catch (Exception e) {
            throw new InternalException("Internal exception on setting unauthorized response", e);
        }

    }
}
