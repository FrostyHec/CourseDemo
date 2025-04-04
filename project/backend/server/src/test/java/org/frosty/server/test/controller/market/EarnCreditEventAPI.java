package org.frosty.server.test.controller.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@RequiredArgsConstructor
public class EarnCreditEventAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String eventBaseUrl = PathConstant.API + "/event";

    public void handleCourseCompleteEvent(String token, CompleteCourseEvent event) throws Exception {
        String url = eventBaseUrl + "/complete-course";
        String json = objectMapper.writeValueAsString(event);
        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .headers(authUtil.setAuthHeader(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(RespChecker.success());
    }

    public void handleDailyCommentEvent(String token, CreateCommentEvent event) throws Exception {
        String url = eventBaseUrl + "/daily-comment";
        String json = objectMapper.writeValueAsString(event);
        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .headers(authUtil.setAuthHeader(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(RespChecker.success());
    }
}
