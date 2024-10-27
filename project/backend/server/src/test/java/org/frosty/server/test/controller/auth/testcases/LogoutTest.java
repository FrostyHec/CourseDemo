package org.frosty.server.test.controller.auth.testcases;

import org.frosty.server.controller.user.AuthController.LoginInfo;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.user.UserAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.frosty.test_common.utils.RespChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class LogoutTest {
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private UserAPI userAPI;

    @Test
    public void testValidLogout() throws Exception {
        String name = "admin";
        String password = "admin";
        var user = userAPI.addSimpleTestUser(name, password, User.Role.admin);
        var loginInfo = new LoginInfo(user.getEmail(), password);
        String token = authAPI.loginSuccess(loginInfo);
        assert user.getUserId() == 1;
        authAPI.logout(token, user.getUserId())
                .andExpect(RespChecker.success());
    }

    @Test
    public void testInvalidToken() throws Exception {
        String name = "admin";
        String password = "admin";
        var user = userAPI.addSimpleTestUser(name, password, User.Role.admin);
        var loginInfo = new LoginInfo(user.getEmail(), password);
        String token = authAPI.loginSuccess(loginInfo);
        authAPI.logout(token, user.getUserId() + 1)
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("unmatched-id"));
    }
}
