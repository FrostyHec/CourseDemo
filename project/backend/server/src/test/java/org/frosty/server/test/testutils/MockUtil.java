package org.frosty.server.test.testutils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@Component
@RequiredArgsConstructor
public class MockUtil {
    @Autowired
    private final AuthUtil authUtil;
    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final MockMvc mockMvc;

    // Extract common success response handling
    public static <T> T getSuccessResponse(ResultActions resultActions, Class<T> responseType) throws Exception {
        var resp = resultActions.andExpect(RespChecker.success()).andReturn();
        return JsonUtils.toObject(resp, responseType);
    }
    public static <T> T getSuccessResponse(ResultActions resultActions, TypeReference<T> responseType) throws Exception {
        var resp = resultActions.andExpect(RespChecker.success()).andReturn();
        return JsonUtils.toObject(resp, responseType);
    }


    // Common performRequest method
    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder,
                                        String token,
                                        Object body) throws Exception {
        if (token != null) {
            requestBuilder.headers(authUtil.setAuthHeader(token)).accept(MediaType.APPLICATION_JSON);
        }
        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(json);
        }
        return mockMvc.perform(requestBuilder);
    }

}
