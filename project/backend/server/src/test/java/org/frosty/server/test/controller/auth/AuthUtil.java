package org.frosty.server.test.controller.auth;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.config.AuthConstant;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.exception.InvalidTokenException;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.auth.utils.TokenUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final JwtHandler jwtHandler;

    public HttpHeaders setAuthHeader(String token) throws InvalidTokenException {
        HttpHeaders headers = new HttpHeaders();
        var parsedToken = TokenInfo.getWithAuthInfo(jwtHandler.getToken(jwtHandler.getClaimsFromToken(token)));
        headers.add(AuthConstant.parsedHeader, TokenUtils.tokenInfoToString(parsedToken));
        return headers;
    }
}
