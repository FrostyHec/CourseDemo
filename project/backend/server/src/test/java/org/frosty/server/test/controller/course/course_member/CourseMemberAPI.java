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

    public ResultActions enrollStudentToCourse(String studentToken, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/student/enroll";
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authAPI.setAuthHeader(studentToken))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void enrollStudentToCourseSuccess(String studentToken, Long courseId) throws Exception {
        enrollStudentToCourse(studentToken, courseId)
                .andExpect(RespChecker.success());
    }

    public ResultActions inviteStudentsToCourse(String teacherToken, Long courseId, List<Long> studentIds) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/teacher/invite";
        String json = objectMapper.writeValueAsString(new CourseMemberController.StudentList(studentIds));
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authAPI.setAuthHeader(teacherToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void inviteStudentsToCourseSuccess(String teacherToken, Long courseId, List<Long> studentIds) throws Exception {
        inviteStudentsToCourse(teacherToken, courseId, studentIds)
                .andExpect(RespChecker.success());
    }

    public ResultActions getStudentList(String token, Long courseId, int pageNum, int pageSize) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/student";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authAPI.setAuthHeader(token))
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<StudentWithEnrollStatus> getStudentListSuccess(String token, Long courseId, int pageNum, int pageSize) throws Exception {
        var resp = getStudentList(token, courseId, pageNum,pageSize)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseMemberController.StudentStatusList.class).getStudentList();
    }

    public ResultActions getStudentCourses(String studentToken, Long studentId, int pageNum, int pageSize) throws Exception {
        String url = PathConstant.API + "/student/" + studentId + "/courses";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authAPI.setAuthHeader(studentToken))
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Course> getStudentCoursesSuccess(String studentToken, Long studentId,int pageNum, int pageSize) throws Exception {
        var resp = getStudentCourses(studentToken, studentId, pageNum,pageSize)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseMemberController.CourseList.class).getContent();
    }

    public ResultActions getTeacherCourses(String teacherToken, Long teacherId, int pageNum, int pageSize) throws Exception {
        String url = PathConstant.API + "/teacher/" + teacherId + "/courses";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authAPI.setAuthHeader(teacherToken))
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Course> getTeacherCoursesSuccess(String teacherToken, Long teacherId, int pageNum, int pageSize) throws Exception {
        var resp = getTeacherCourses(teacherToken, teacherId, pageNum,pageSize)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseMemberController.CourseList.class).getContent();
    }

    public ResultActions getStudentStatus(String token, Long courseId, Long studentId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/student/" + studentId + "/status";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authAPI.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public Enrollment.EnrollmentType getStudentStatusSuccess(String token, Long courseId, Long studentId) throws Exception {
        var resp = getStudentStatus(token, courseId, studentId)
                .andExpect(RespChecker.success())
                .andReturn();
        return (Enrollment.EnrollmentType) JsonUtils.toObject(resp, Map.class).get("status");
    }

    public ResultActions getSubmittedCourses(String adminToken, Long adminId, int pageNum, int pageSize) throws Exception {
        String url = PathConstant.API + "/admin/" + adminId + "/courses/submitted";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authAPI.setAuthHeader(adminToken))
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Course> getSubmittedCoursesSuccess(String adminToken, Long adminId, int pageNum, int pageSize) throws Exception {
        var resp = getSubmittedCourses(adminToken, adminId, pageNum,pageSize)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseMemberController.CourseList.class).getContent();
    }
}
