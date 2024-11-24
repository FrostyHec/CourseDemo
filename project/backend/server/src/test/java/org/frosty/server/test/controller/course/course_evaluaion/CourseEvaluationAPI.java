package org.frosty.server.test.controller.course.course_evaluaion;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CourseEvaluationController;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.test.testutils.MockUtil;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseEvaluationAPI {
    private final String courseBaseUrl = PathConstant.API + "/course";
    private final MockUtil mockUtil;

    // Create course evaluation
    public ResultActions createEvaluation(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        return mockUtil.performRequest(requestBuilder, token, courseEvaluation);
    }

    public void createEvaluationSuccess(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        createEvaluation(token, courseId, courseEvaluation).andExpect(RespChecker.success());
    }

    // Update course evaluation
    public ResultActions updateEvaluation(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(url);
        return mockUtil.performRequest(requestBuilder, token, courseEvaluation);
    }

    public void updateEvaluationSuccess(String token, Long courseId, CourseEvaluation courseEvaluation) throws Exception {
        updateEvaluation(token, courseId, courseEvaluation).andExpect(RespChecker.success());
    }

    // Delete course evaluation
    public ResultActions deleteEvaluation(String token, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public void deleteEvaluationSuccess(String token, Long courseId) throws Exception {
        deleteEvaluation(token, courseId).andExpect(RespChecker.success());
    }

    // Get course evaluation
    public ResultActions getEvaluation(String token, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluation";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public CourseEvaluation getEvaluationSuccess(String token, Long courseId) throws Exception {
        var resultActions = getEvaluation(token, courseId);
        return MockUtil.getSuccessResponse(resultActions, CourseEvaluation.class);
    }

    // Get all course evaluations
    public ResultActions getAllEvaluations(String token, Long courseId, int pageSize, int pageNum) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/evaluations";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("page_size", String.valueOf(pageSize))
                .param("page_num", String.valueOf(pageNum));
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public List<CourseEvaluation> getAllEvaluationsSuccess(String token, Long courseId, int pageSize, int pageNum) throws Exception {
        var resultActions = getAllEvaluations(token, courseId, pageSize, pageNum);
        return MockUtil.getSuccessResponse(resultActions, CourseEvaluationController.CourseEvaluationList.class).getContent();
    }
}
