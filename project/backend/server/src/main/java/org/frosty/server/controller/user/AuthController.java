package org.frosty.server.controller.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.services.user.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(PathConstant.API + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginInfo loginInfo) {
        String token = authService.login(loginInfo);
        return Map.of("token", token);
    }

    @PostMapping("/logout")
    public void logout(@GetToken TokenInfo tokenInfo, @RequestBody LogoutInfo logoutInfo) {
        Ex.check(logoutInfo.userId == tokenInfo.getAuthInfo().getUserID(),
                Response.getBadRequest("unmatched-id"));
        authService.logout(tokenInfo);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogoutInfo {
        private long userId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginInfo {
        private long userId;
        private String password;
    }
}
