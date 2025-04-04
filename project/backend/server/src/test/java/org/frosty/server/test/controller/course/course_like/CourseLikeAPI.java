package org.frosty.server.test.controller.course.course_like;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.test.testutils.MockUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CourseLikeAPI {
    private final MockUtil mockUtil;
    private final String baseUrl = PathConstant.API + "/course";

    // Create course like
    public ResultActions createCourseLike(String token, Long courseId, Long studentId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(baseUrl + "/" + courseId + "/like")
                .param("userId", String.valueOf(studentId));
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public void createCourseLikeSuccess(String token, Long courseId, Long studentId) throws Exception {
        createCourseLike(token, courseId, studentId).andExpect(RespChecker.success());
    }

    // Delete course like
    public ResultActions deleteCourseLike(String token, Long courseId, Long studentId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(baseUrl + "/" + courseId + "/like")
                .param("userId", String.valueOf(studentId));
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public void deleteCourseLikeSuccess(String token, Long courseId, Long studentId) throws Exception {
        deleteCourseLike(token, courseId, studentId).andExpect(RespChecker.success());
    }

    // Check course like
    public ResultActions checkCourseLike(String token, Long courseId, Long studentId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(baseUrl + "/" + courseId + "/like")
                .param("userId", String.valueOf(studentId));
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public Map<String,Boolean> checkCourseLikeSuccess(String token, Long courseId, Long studentId) throws Exception {
        var resultActions = checkCourseLike(token, courseId, studentId);
        return MockUtil.getSuccessResponse(resultActions, new TypeReference<>() {
        });
    }
}