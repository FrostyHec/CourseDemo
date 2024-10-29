package org.frosty.server.test.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.user.UserController;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.mapper.user.UserMapper;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAPI {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final MockMvc mockMvc;
    private final String courseBaseUrl = PathConstant.API;
    private final AuthUtil authUtil;
    private final ObjectMapper objectMapper;

    // 提取通用的Success处理
    public static <T> T getSuccessResponse(ResultActions resultActions, Class<T> responseType) throws Exception {
        var resp = resultActions.andExpect(RespChecker.success()).andReturn();
        return JsonUtils.toObject(resp, responseType);
    }

    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder, String token, Object body) throws Exception {
        if (token != null)
            requestBuilder.headers(authUtil.setAuthHeader(token)).accept(MediaType.APPLICATION_JSON);

        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(json);
        }
        return mockMvc.perform(requestBuilder);
    }

    public ResultActions registerUser(User user, String confirmPassword) throws Exception {
        String url = courseBaseUrl + "/user";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        return performRequest(requestBuilder, null, user);
    }

    public void registerUserSuccess(User user, String confirmPassword) throws Exception {
        registerUser(user, confirmPassword).andExpect(RespChecker.success());
    }

    public ResultActions updateUser(String token, Long userId, User updatedUser) throws Exception {
        String url = courseBaseUrl + "/user/" + userId;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(url);
        return performRequest(requestBuilder, token, updatedUser);
    }

    public void updateUserSuccess(String token, Long userId, User updatedUser) throws Exception {
        updateUser(token, userId, updatedUser).andExpect(RespChecker.success());
    }

    public ResultActions getUser(String token, Long userId) throws Exception {
        String url = courseBaseUrl + "/user/" + userId;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return performRequest(requestBuilder, token, null);
    }

    public User getUserSuccess(String token, Long userId) throws Exception {
        return getSuccessResponse(getUser(token, userId), User.class);
    }

    public ResultActions deleteUser(String token, Long userId) throws Exception {
        String url = courseBaseUrl + "/user/" + userId;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        return performRequest(requestBuilder, token, null);
    }

    public void deleteUserSuccess(String token, Long userId) throws Exception {
        deleteUser(token, userId).andExpect(RespChecker.success());
    }

    public ResultActions searchByRealName(String token, String realName) throws Exception {
        String url = courseBaseUrl + "/user/search";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .param("realName", realName); // realName?
        return performRequest(requestBuilder, token, null);
    }

    public List<User> searchByRealNameSuccess(String token, String realName) throws Exception {
        return getSuccessResponse(searchByRealName(token, realName), UserController.UserList.class).getContent();
    }

    public ResultActions getUserPublicInfo(String token, Long userId) throws Exception {
        String url = courseBaseUrl + "/user/public/" + userId;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return performRequest(requestBuilder, token, null);
    }

    public UserPublicInfo getUserPublicInfoSuccess(String token, Long userId) throws Exception {
        return getSuccessResponse(getUserPublicInfo(token, userId), UserPublicInfo.class);
    }

    public User addSimpleTestUser(String firstName, String password, User.Role role) {

        var user = new User()
                .setFirstName(firstName)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role)
                .setEmail(firstName + "@test.com");
        return addTestUser(user);
    }

    public User addTestUser(User user) {
        mapper.insert(user);
        return user;
    }

    public void checkPublicUserEquality(User origin, UserPublicInfo info) {
        assert origin.getUserId().equals(info.getUserId());
        assert origin.getFirstName().equals(info.getFirstName());
        assert origin.getLastName() == null || origin.getLastName().equals(info.getLastName());
        assert origin.getRole().equals(info.getRole());
        assert origin.getEmail().equals(info.getEmail());
    }

}
