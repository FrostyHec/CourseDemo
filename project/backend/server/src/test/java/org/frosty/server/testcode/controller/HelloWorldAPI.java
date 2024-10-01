package org.frosty.server.testcode.controller;

import org.frosty.common.constant.PathConstant;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelloWorldAPI {
    private final MockMvc mockMvc;

    public ResultActions say(String say) throws Exception {
        String baseUrl = PathConstant.API + "/hello";
        return mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                                                     .param("say",say)
                                                     .accept(MediaType.APPLICATION_JSON));
    }
}
