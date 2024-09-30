package org.forsty.auth.entity;

import lombok.Data;

@Data
public class TokenInfo {
    private AuthStatus authStatus;
    private AuthInfo authInfo;
}
