package org.frosty.server.test.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.utils.Pair;
import org.frosty.server.controller.user.AuthController.LoginInfo;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.user.UserAPI;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserAPI userAPI;
    private final AuthUtil authUtil;
    private final String baseUrl = PathConstant.API + "/auth";

    public ResultActions login(LoginInfo loginInfo) throws Exception {
        String url = baseUrl + "/login";
        String json = objectMapper.writeValueAsString(loginInfo);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions logout(String token, long userId) throws Exception {
        String url = baseUrl + "/logout";
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toString(Map.of("user_id", userId)))
                .accept(MediaType.APPLICATION_JSON));
    }

    public String loginSuccess(LoginInfo loginInfo) throws Exception {
        var resp = login(loginInfo)
                .andExpect(RespChecker.success())
                .andReturn();
        var token = (String) JsonUtils.toMapData(resp).get("token");
        assert token != null;
        return token;
    }

    public Pair<String, User> quickAddUserAndLogin(String name, User.Role role) throws Exception {
        var password = "123456";
        var user = userAPI.addSimpleTestUser(name, password, role);
        return new Pair<>(loginSuccess(new LoginInfo(user.getUserId(), password)), user);
    }
}
