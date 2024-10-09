package org.frosty.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.exception.InternalException;

public class TokenUtils {
    public static String tokenInfoToString(TokenInfo tokenInfo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(tokenInfo);
        } catch (Exception e) {
            throw new InternalException("Failed to convert tokenInfo to string", e);
        }
    }

    public static TokenInfo tokenInfoFromString(String subject) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(subject, TokenInfo.class);
        } catch (Exception e) {
            throw new InternalException("Failed to convert string to tokenInfo", e);
        }
    }

    public static String authInfoToString(AuthInfo auth) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(auth);
        } catch (Exception e) {
            throw new InternalException("Failed to convert authInfo to string", e);
        }
    }

    public static AuthInfo authInfoFromString(String subject) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(subject, AuthInfo.class);
        } catch (Exception e) {
            throw new InternalException("Failed to convert string to authInfo", e);
        }
    }
}
