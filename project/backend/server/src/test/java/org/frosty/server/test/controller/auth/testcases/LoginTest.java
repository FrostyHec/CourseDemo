package org.frosty.server.test.controller.auth.testcases;

import org.frosty.auth.utils.JwtHandler;
import org.frosty.server.entity.bo.User;
import org.frosty.server.controller.auth.AuthController.LoginInfo;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.user.UserAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.frosty.test_common.utils.RespChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class LoginTest {
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private UserAPI userAPI;
    @Autowired
    private JwtHandler jwtHandler;
    @Test
    public void testValidLogin() throws Exception {
        String name = "admin";
        String password = "admin";
        var user = userAPI.addTestUser(name,password, User.Role.admin);
        var loginInfo = new LoginInfo(user.getUserId(),password);
        String token = authAPI.loginSuccess(loginInfo);
        assert jwtHandler.getClaimsFromToken(token)!=null;//token valid
    }

    @Test
    public void testIncorrectPassword() throws Exception {
        String name = "admin";
        String password = "admin";
        var user = userAPI.addTestUser(name,password, User.Role.admin);
        var loginInfo = new LoginInfo(user.getUserId(),"wrong-password");
        authAPI.login(loginInfo)
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("incorrect-password"));
    }

    @Test
    public void testUserNotFound() throws Exception {
        var loginInfo = new LoginInfo(0,"wrong-password");
        authAPI.login(loginInfo)
                .andExpect(RespChecker.noFound())
                .andExpect(RespChecker.message("user-not-found"));
    }

}
