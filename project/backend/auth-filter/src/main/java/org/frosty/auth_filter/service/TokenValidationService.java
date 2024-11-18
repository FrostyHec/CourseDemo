package org.frosty.auth_filter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.config.AuthConstant;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.exception.InvalidTokenException;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.common.exception.ExternalExceptionOnResponseEntity;
import org.frosty.common.response.Response;
import org.frosty.common_service.storage.api.SharedBiMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationService {
    private final JwtHandler jwtHandler;
    private final SharedBiMapService sharedBiMapService;
    public AuthInfo validateToken(String token) {
        Claims claims;
        try {
            if (!token.startsWith("Bearer ")) {
                throw new InvalidTokenException("token with incorrect format", null);
            }
            claims = jwtHandler.getClaimsFromToken(token.substring(7));
        } catch (InvalidTokenException e) {
            throw new ExternalExceptionOnResponseEntity(
                    ResponseEntity.status(AuthConstant.internalUnauthorizedCode)
                            .body(Response.getUnauthorized("invalid-auth")));
        }
        // check if token is expired
        if (jwtHandler.isTokenExpired(claims)) {
            throw new ExternalExceptionOnResponseEntity(
                    ResponseEntity.status(AuthConstant.internalUnauthorizedCode)
                            .body(Response.getUnauthorized("auth-expired")));
        }
        // check if token in pool
        if (!isTokenInPool(token)) {
            throw new ExternalExceptionOnResponseEntity(
                    ResponseEntity.status(AuthConstant.internalUnauthorizedCode)
                            .body(Response.getUnauthorized("invalid-auth")));
        }

        return jwtHandler.getToken(claims);
    }
    private boolean isTokenInPool(String token) {
        return true;// TODO using redis for token pool sharing
//        return sharedBiMapService.valueExist(token);
    }
}
