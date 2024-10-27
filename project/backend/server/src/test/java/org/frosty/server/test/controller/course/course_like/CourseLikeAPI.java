package org.frosty.server.test.controller.course.course_like;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@RequiredArgsConstructor
public class CourseLikeAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final org.frosty.server.test.controller.auth.AuthUtil authUtil;
    private final String baseUrl = PathConstant.API + "/course";

    // Common performRequest method
    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder, String token, Object body) throws Exception {
        requestBuilder.headers(authUtil.setAuthHeader(token)).accept(MediaType.APPLICATION_JSON);
        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(json);
        }
        return mockMvc.perform(requestBuilder);
    }

    // Common success response handler
    public static <T> T getSuccessResponse(ResultActions resultActions, Class<T> responseType) throws Exception {
        var resp = resultActions.andExpect(RespChecker.success()).andReturn();
        return JsonUtils.toObject(resp, responseType);
    }

    // Create course like
    public ResultActions createCourseLike(String token, Long courseId, Long studentId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(baseUrl + "/" + courseId + "/like")
                .param("userId", String.valueOf(studentId));
        return performRequest(requestBuilder, token, null);
    }

    public void createCourseLikeSuccess(String token, Long courseId, Long studentId) throws Exception {
        createCourseLike(token, courseId, studentId).andExpect(RespChecker.success());
    }

    // Delete course like
    public ResultActions deleteCourseLike(String token, Long courseId, Long studentId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(baseUrl + "/" + courseId + "/like")
                .param("userId", String.valueOf(studentId));
        return performRequest(requestBuilder, token, null);
    }

    public void deleteCourseLikeSuccess(String token, Long courseId, Long studentId) throws Exception {
        deleteCourseLike(token, courseId, studentId).andExpect(RespChecker.success());
    }

    // Check course like
    public ResultActions checkCourseLike(String token, Long courseId, Long studentId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(baseUrl + "/" + courseId + "/like")
                .param("userId", String.valueOf(studentId));
        return performRequest(requestBuilder, token, null);
    }

    public boolean checkCourseLikeSuccess(String token, Long courseId, Long studentId) throws Exception {
        var resultActions = checkCourseLike(token, courseId, studentId);
        return getSuccessResponse(resultActions, Boolean.class);
    }
}