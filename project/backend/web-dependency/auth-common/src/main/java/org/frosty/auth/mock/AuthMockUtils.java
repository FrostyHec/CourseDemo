package org.frosty.auth.mock;

import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.TokenUtils;

public class AuthMockUtils {
    public static String getPassTokenInfoString(Long uid) {
        return TokenUtils.tokenInfoToString(
                new TokenInfo(AuthStatus.PASS, new AuthInfo(uid)));
    }
}
