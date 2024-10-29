package org.frosty.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.AuthStatus;
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
        if(subject==null){// TODO REMOVE
            return new TokenInfo(AuthStatus.PASS,new AuthInfo(1));

        }
        if (subject != null && subject.startsWith("Bearer ")) { // TODO REMOVE
            JwtHandler jwtHandler = new JwtHandler("secret12740184018403rujdfcu9hv9nsdzsajsz0e" +
                    "hfUO(vhqoCQfsecret12740184018403rujdfcu9hv9nsdzsajsz0ehfUO(vhqoCQf", 86400000);
            try {
                var claims = jwtHandler.getClaimsFromToken(subject.substring(7));
                return new TokenInfo(AuthStatus.PASS,jwtHandler.getToken(claims));
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(subject, TokenInfo.class);
        } catch (Exception e) {
            throw new InternalException("Failed to convert string to tokenInfo subject:" + subject, e);
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
