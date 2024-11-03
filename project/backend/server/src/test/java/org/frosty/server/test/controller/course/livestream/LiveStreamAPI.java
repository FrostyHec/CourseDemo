package org.frosty.server.test.controller.course.livestream;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.livestream.LiveStreamController;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Component
@RequiredArgsConstructor
public class LiveStreamAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String baseUrl = PathConstant.API + "/course";
    private final String authBaseUrl = PathConstant.INTERNAL_API + "/auth/live-stream/course";

    public ResultActions getPushName(String token, Long courseId) throws Exception {
        String url = baseUrl + "/" + courseId + "/live-stream/push";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public LiveStreamController.LivestreamName getPushNameSuccess(String token, Long courseId) throws Exception {
        var resp = getPushName(token, courseId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, LiveStreamController.LivestreamName.class);
    }

    public ResultActions getPullName(String token, Long courseId) throws Exception {
        String url = baseUrl + "/" + courseId + "/live-stream/pull";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public LiveStreamController.LivestreamName getPullNameSuccess(String token, Long courseId) throws Exception {
        var resp = getPullName(token, courseId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, LiveStreamController.LivestreamName.class);
    }


    public ResultActions getPushAuth(String name) throws Exception {
        String url = authBaseUrl + "/push/" + name;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void getPushAuthSuccess(String name) throws Exception {
        getPushAuth(name)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));
    }

    public ResultActions getPullAuth(String token, String name) throws Exception {
        String uri = "http://example.com:8080/live&app=course&stream="+name;
        String url = authBaseUrl + "/pull";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .header("X-Original-URI", uri)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void getPullAuthSuccess(String token, String uri) throws Exception {
        getPullAuth(token, uri)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));
    }
}
