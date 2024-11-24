package org.frosty.auth.utils;

import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;

public class AuthEx {

    public static AuthInfo checkPass(TokenInfo tokenInfo){
        Ex.check(tokenInfo.getAuthStatus()== AuthStatus.PASS,Response.getUnauthorized("unauthorized"));
        return tokenInfo.getAuthInfo();
    }
}
