package org.frosty.server.test.controller.course.recommend;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.RecommendController;
import org.frosty.server.test.testutils.MockUtil;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RecommendAPI {
    private final String baseUrl = PathConstant.API + "/recommend";
    private final MockUtil mockUtil;

    // 获取热门课程
    public ResultActions getHotCourses(String token, int pageSize, int pageNum) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(baseUrl + "/courses/hot")
                .param("page_size", String.valueOf(pageSize))
                .param("page_num", String.valueOf(pageNum));
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public Map<String, List<RecommendController.CourseWithStudentCount>> getHotCoursesSuccess(String token, int pageSize, int pageNum) throws Exception {
        var resultActions = getHotCourses(token, pageSize, pageNum);
        return MockUtil.getSuccessResponse(resultActions,
                new TypeReference<Map<String, List<RecommendController.CourseWithStudentCount>>>() {
                });
    }

    // 获取热门老师
    public ResultActions getHotTeachers(String token, int pageSize, int pageNum) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(baseUrl + "/teachers/hot")
                .param("page_size", String.valueOf(pageSize))
                .param("page_num", String.valueOf(pageNum));
        return mockUtil.performRequest(requestBuilder, token, null);
    }

    public Map<String, List<RecommendController.TeacherWithStudentCount>> getHotTeachersSuccess(String token, int pageSize, int pageNum) throws Exception {
        var resultActions = getHotTeachers(token, pageSize, pageNum);
        return MockUtil.getSuccessResponse(resultActions,
                new TypeReference<Map<String, List<RecommendController.TeacherWithStudentCount>>>() {
                });
    }
}
