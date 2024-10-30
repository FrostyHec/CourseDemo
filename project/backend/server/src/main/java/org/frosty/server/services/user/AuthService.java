package org.frosty.server.services.user;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.controller.user.AuthController.LoginInfo;
import org.frosty.server.controller.user.AuthController.LoginSuccessInfo;
import org.frosty.server.entity.converter.CommonConverter;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper mapper;
    private final JwtHandler jwtHandler;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private CommonConverter converter;

    public LoginSuccessInfo login(LoginInfo loginInfo) {
        var user = mapper.selectByEmail(loginInfo.getEmail());
        Ex.check(user != null, Response.getNotFound("user-not-found"));
        Ex.check(passwordEncoder.matches(loginInfo.getPassword(), user.getPassword()),
                Response.getBadRequest("incorrect-password"));
        String token = jwtHandler.createToken(new AuthInfo(user.getUserId()));
//        var publicInfo = converter.toUserPublicInfo(user);
        var publicInfo = converter.toUserPublicInfo(user);
        return new LoginSuccessInfo(publicInfo, token);
    }

    public void logout(TokenInfo tokenInfo) {
        // 可能需要撤回token的发放
    }
}
