package org.frosty.server.services.auth;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.controller.auth.AuthController.LoginInfo;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper mapper;
    private final JwtHandler jwtHandler;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginInfo loginInfo) {
        var user = mapper.selectById(loginInfo.getUserId());
        Ex.check(user != null, Response.getNotFound("user-not-found"));
        Ex.check(passwordEncoder.matches(loginInfo.getPassword(), user.getPassword()),
                Response.getBadRequest("incorrect-password"));
        return jwtHandler.createToken(new AuthInfo(user.getUserId()));
    }

    public void logout(TokenInfo tokenInfo) {
        // 可能需要撤回token的发放
    }
}
