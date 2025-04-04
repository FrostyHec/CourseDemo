package org.frosty.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    private AuthStatus authStatus;
    private AuthInfo authInfo;

    public static TokenInfo getWithoutAuthInfo() {
        return new TokenInfo(AuthStatus.EMPTY, null);
    }

    public static TokenInfo getWithAuthInfo(AuthInfo authInfo) {
        return new TokenInfo(AuthStatus.PASS, authInfo);
    }
}
