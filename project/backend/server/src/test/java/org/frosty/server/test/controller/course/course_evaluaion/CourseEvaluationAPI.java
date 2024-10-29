package org.frosty.server.test.controller.course.course_evaluaion;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CourseEvaluationController;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseEvaluationAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil AuthUtil;
    private final String courseBaseUrl = PathConstant.API + "/course";

    // Extract common success response handling
    public static <T> T getSuccessResponse(ResultActions resultActions, Class<T> responseType) throws Exception {
        var resp = resultActions.andExpect(RespChecker.success()).andReturn();
        return JsonUtils.toObject(resp, responseType);
    }

    // Common performRequest method
    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder, String token, Object body) throws Exception {
        requestBuilder.headers(AuthUtil.setAuthHeader(token)).accept(MediaType.APPLICATION_JSON);
        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(json);
        }
        return mockMvc.perform(requestBuilder);
    }

    // Create course evaluation
    public ResultActions createEvaluation(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        return performRequest(requestBuilder, token, courseEvaluation);
    }

    public void createEvaluationSuccess(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        createEvaluation(token, courseId, courseEvaluation).andExpect(RespChecker.success());
    }

    // Update course evaluation
    public ResultActions updateEvaluation(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(url);
        return performRequest(requestBuilder, token, courseEvaluation);
    }

    public void updateEvaluationSuccess(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        updateEvaluation(token, courseId, courseEvaluation).andExpect(RespChecker.success());
    }

    // Delete course evaluation
    public ResultActions deleteEvaluation(String token, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        return performRequest(requestBuilder, token, null);
    }

    public void deleteEvaluationSuccess(String token, Long courseId) throws Exception {
        deleteEvaluation(token, courseId).andExpect(RespChecker.success());
    }

    // Get course evaluation
    public ResultActions getEvaluation(String token, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return performRequest(requestBuilder, token, null);
    }

    public CourseEvaluation getEvaluationSuccess(String token, Long courseId) throws Exception {
        var resultActions = getEvaluation(token, courseId);
        return getSuccessResponse(resultActions, CourseEvaluation.class);
    }

    // Get all course evaluations
    public ResultActions getAllEvaluations(String token, Long courseId, int pageSize, int pageNum) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluations";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("page_size", String.valueOf(pageSize))
                .param("page_num", String.valueOf(pageNum));
        return performRequest(requestBuilder, token, null);
    }

    public List<CourseEvaluation> getAllEvaluationsSuccess(String token, Long courseId, int pageSize, int pageNum) throws Exception {
        var resultActions = getAllEvaluations(token, courseId, pageSize, pageNum);
        return getSuccessResponse(resultActions, CourseEvaluationController.CourseEvaluationList.class).getContent();
    }
}
