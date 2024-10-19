package org.frosty.auth_filter.testcode.controller;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@RequiredArgsConstructor
public class AuthFilterAPI {
    private final MockMvc mockMvc;
    public String exampleData = "{\"example\":\"data\"}";

    public ResultActions auth(HttpMethod method, String token, String data) throws Exception {
        String baseUrl = PathConstant.INTERNAL_API + "/auth";
        return mockMvc.perform(MockMvcRequestBuilders.request(method, baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions auth(HttpMethod method, String token) throws Exception {
        return auth(method, token, exampleData);
    }
}
