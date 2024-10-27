package org.frosty.server.test.controller.course.course_member;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CourseMemberController;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.Enrollment;
import org.frosty.server.entity.po.StudentWithEnrollStatus;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CourseMemberAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthAPI authAPI;
    private final String courseBaseUrl = PathConstant.API + "/course";

    // 通用的performRequest方法
    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder, String token, Object body) throws Exception {
        requestBuilder.headers(authAPI.setAuthHeader(token)).accept(MediaType.APPLICATION_JSON);
        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(json);
        }
        return mockMvc.perform(requestBuilder);
    }

    // 提取通用的Success处理
    public static <T> T getSuccessResponse(ResultActions resultActions, Class<T> responseType) throws Exception {
        var resp = resultActions.andExpect(RespChecker.success()).andReturn();
        return JsonUtils.toObject(resp, responseType);
    }

    // 学生加入课程
    public ResultActions enrollStudentToCourse(String studentToken, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/student/enroll";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        return performRequest(requestBuilder, studentToken, null);
    }

    public void enrollStudentToCourseSuccess(String studentToken, Long courseId) throws Exception {
        enrollStudentToCourse(studentToken, courseId).andExpect(RespChecker.success());
    }

    // 邀请学生加入课程
    public ResultActions inviteStudentsToCourse(String teacherToken, Long courseId, List<Long> studentIds) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/teacher/invite";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        return performRequest(requestBuilder, teacherToken, new CourseMemberController.StudentList(studentIds));
    }

    public void inviteStudentsToCourseSuccess(String teacherToken, Long courseId, List<Long> studentIds) throws Exception {
        inviteStudentsToCourse(teacherToken, courseId, studentIds).andExpect(RespChecker.success());
    }

    // 获取学生列表
    public ResultActions getStudentList(String token, Long courseId, int pageNum, int pageSize) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/student";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize));
        return performRequest(requestBuilder, token, null);
    }

    public List<StudentWithEnrollStatus> getStudentListSuccess(String token, Long courseId, int pageNum, int pageSize) throws Exception {
        var resultActions = getStudentList(token, courseId, pageNum, pageSize);
        return getSuccessResponse(resultActions, CourseMemberController.StudentStatusList.class).getStudentList();
    }

    // 获取学生加入的课程
    public ResultActions getStudentCourses(String studentToken, Long studentId, int pageNum, int pageSize) throws Exception {
        String url = PathConstant.API + "/student/" + studentId + "/courses";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize));
        return performRequest(requestBuilder, studentToken, null);
    }

    public List<Course> getStudentCoursesSuccess(String studentToken, Long studentId, int pageNum, int pageSize) throws Exception {
        var resultActions = getStudentCourses(studentToken, studentId, pageNum, pageSize);
        return getSuccessResponse(resultActions, CourseMemberController.CourseList.class).getContent();
    }

    // 获取教师课程
    public ResultActions getTeacherCourses(String teacherToken, Long teacherId, int pageNum, int pageSize) throws Exception {
        String url = PathConstant.API + "/teacher/" + teacherId + "/courses";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize));
        return performRequest(requestBuilder, teacherToken, null);
    }

    public List<Course> getTeacherCoursesSuccess(String teacherToken, Long teacherId, int pageNum, int pageSize) throws Exception {
        var resultActions = getTeacherCourses(teacherToken, teacherId, pageNum, pageSize);
        return getSuccessResponse(resultActions, CourseMemberController.CourseList.class).getContent();
    }

    // 获取学生在课程中的状态
    public ResultActions getStudentStatus(String token, Long courseId, Long studentId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/student/" + studentId + "/status";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return performRequest(requestBuilder, token, null);
    }

    public Enrollment.EnrollmentType getStudentStatusSuccess(String token, Long courseId, Long studentId) throws Exception {
        var resultActions = getStudentStatus(token, courseId, studentId);
        Map<String, Object> responseMap = getSuccessResponse(resultActions, Map.class);
        return Enrollment.EnrollmentType.valueOf((String) responseMap.get("status"));
    }

    // 获取提交的课程
    public ResultActions getSubmittedCourses(String adminToken, Long adminId, int pageNum, int pageSize) throws Exception {
        String url = PathConstant.API + "/admin/" + adminId + "/courses/submitted";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize));
        return performRequest(requestBuilder, adminToken, null);
    }

    public List<Course> getSubmittedCoursesSuccess(String adminToken, Long adminId, int pageNum, int pageSize) throws Exception {
        var resultActions = getSubmittedCourses(adminToken, adminId, pageNum, pageSize);
        return getSuccessResponse(resultActions, CourseMemberController.CourseList.class).getContent();
    }
}
