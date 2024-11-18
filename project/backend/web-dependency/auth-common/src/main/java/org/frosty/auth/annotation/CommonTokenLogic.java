package org.frosty.auth.annotation;

import lombok.extern.slf4j.Slf4j;
import org.frosty.auth.config.AuthConstant;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.auth.utils.TokenUtils;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.springframework.web.context.request.NativeWebRequest;
@Slf4j
public class CommonTokenLogic {
    private static boolean noFilter = true;
    public static TokenInfo extractToken(NativeWebRequest webRequest){
        if (noFilter && webRequest.getHeader(AuthConstant.parsedHeader) == null) {
            String subject = webRequest.getHeader("Authorization");
//            if (subject == null) {// TODO REMOVE
//                return new TokenInfo(AuthStatus.PASS, new AuthInfo(1));
//
//            }
            if (subject == null || !subject.startsWith("Bearer ")) {
                throw new ExternalException(Response.getBadRequest("auth invalid:" + subject));
            }
            JwtHandler jwtHandler = new JwtHandler("secret12740184018403rujdfcu9hv9nsdzsajsz0e" +
                    "hfUO(vhqoCQfsecret12740184018403rujdfcu9hv9nsdzsajsz0ehfUO(vhqoCQf", 86400000);
            try {
                var claims = jwtHandler.getClaimsFromToken(subject.substring(7));
                return new TokenInfo(AuthStatus.PASS, jwtHandler.getToken(claims));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //正常情况
        String token = webRequest.getHeader(AuthConstant.parsedHeader);
        try {
            return TokenUtils.tokenInfoFromString(token);
        } catch (Exception e) {
            log.warn("X-Forwarded-User invalid:{}", token, e);
            throw new ExternalException(Response.getBadRequest(AuthConstant.parsedHeader + " invalid:" + token));
        }
    }
}
