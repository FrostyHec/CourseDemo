package org.frosty.server.test.controller.course.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CourseController;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.mapper.course.CourseMapper;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.server.test.tools.CommonCheck;
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
public class CourseAPI {
    private final MockMvc mockMvc;
    private final CourseMapper mapper;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String courseBaseUrl = PathConstant.API + "/course";

    public Course getTemplateCourse(Long teacherId) {
        return new Course()
                .setTeacherId(teacherId)
                .setCourseName("Course Name")
                .setDescription("Course Description")
                .setStatus(Course.CourseStatus.creating) // it was "approved"
                .setPublication(Course.PublicationType.open)
                ;
    }

    public Course getTemplatePublishedCourse(Long teacherId, String name, Course.PublicationType publicationType) {
        return new Course()
                .setTeacherId(teacherId)
                .setCourseName(name)
                .setDescription("Course Description")
                .setStatus(Course.CourseStatus.published) // it was "approved"
                .setPublication(publicationType)
                ;
    }

    public Long addTestCourseAndGetId(Long teacherId) {
        return addTestCourse(teacherId).getCourseId();
    }

    public Course addTestCourse(Long teacherId) {
        var entity = getTemplateCourse(teacherId);
        mapper.insert(entity);
        return entity;
    }

    public ResultActions adminGetRequiredApprovedCourse(String adminToken, Long userId, int pageSize, int pageNum) throws Exception {
        String url = PathConstant.API + "/admin/" + userId + "/courses/submitted";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(adminToken))
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Course> adminGetAllRequiredApprovedCourseSuccess(String adminToken, Long userId) throws Exception {
        var resp = adminGetRequiredApprovedCourse(adminToken, userId, -1, 1)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseController.CourseList.class).getContent();
    }

    public ResultActions getAllTeachingCourse(String teacherToken, Long userId, int pageSize, int pageNum) throws Exception {
        String url = PathConstant.API + "/teacher/" + userId + "/courses";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(teacherToken))
                .param("page_num", String.valueOf(pageNum))
                .param("page_size", String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Course> getAllTeachingCourseSuccess(String teacherToken, Long userId) throws Exception {
        var resp = getAllTeachingCourse(teacherToken, userId, -1, 1)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseController.CourseList.class).getContent();
    }

    public ResultActions create(String token, Course course) throws Exception {
        String json = objectMapper.writeValueAsString(course);
        return mockMvc.perform(MockMvcRequestBuilders.post(courseBaseUrl)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void createSuccess(String teacherToken, Course course) throws Exception {
        create(teacherToken, course)
                .andExpect(RespChecker.success());
    }

    public ResultActions update(String token, Long cid, Course course) throws Exception {
        String json = objectMapper.writeValueAsString(course);
        return mockMvc.perform(MockMvcRequestBuilders.put(courseBaseUrl + "/" + cid)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateSuccess(String teacherToken, Long cid, Course course) throws Exception {
        update(teacherToken, cid, course)
                .andExpect(RespChecker.success());
    }

    public ResultActions updateStatus(String teacherToken, Long cid, Map<String, String> courseStatus) throws Exception {
        String json = objectMapper.writeValueAsString(courseStatus);
        return mockMvc.perform(MockMvcRequestBuilders.patch(courseBaseUrl + "/" + cid + "/status") // add "/status" to the url, because it is a PATCH request
                .headers(authUtil.setAuthHeader(teacherToken))
                .contentType(MediaType.APPLICATION_JSON)
//                .param("status", status)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateStatusSuccess(String teacherToken, Long courseId, Map<String, String> courseStatus) throws Exception {
        updateStatus(teacherToken, courseId, courseStatus)
                .andExpect(RespChecker.success());
    }


    public ResultActions searchPublicCourse(String token, int page_num, int page_size, String name) throws Exception {
        String url = courseBaseUrl + "/search";
        return mockMvc.perform(MockMvcRequestBuilders.get(url) // add "/status" to the url, because it is a PATCH request
                .headers(authUtil.setAuthHeader(token))
                .param("page_num", String.valueOf(page_num))
                .param("page_size", String.valueOf(page_size))
                .param("name", name)
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Course> searchPublicCourseSuccess(String token, int page_num, int page_size, String name) throws Exception {
        var resp = searchPublicCourse(token, page_num, page_size, name)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseController.CourseList.class).getContent();
    }

    // TODO find by Id
    // delete
    // update publication,

    public void checkSingle(Course origin, List<Course> rcvdLi, Course.CourseStatus targetStatus) {
        var rcvdCourse = CommonCheck.checkSingleAndGet(rcvdLi);
        checkSingle(origin, rcvdCourse, targetStatus);
    }

    public void checkSingle(Course origin, Course rcvdCourse, Course.CourseStatus targetStatus) {
        assert rcvdCourse.getCourseName().equals(origin.getCourseName());
        assert rcvdCourse.getDescription().equals(origin.getDescription());
        assert rcvdCourse.getTeacherId().equals(origin.getTeacherId());
        assert rcvdCourse.getStatus().equals(targetStatus);
    }


    public void quickCreateCourse(Course course) {
        mapper.insertCourse(course);
    }
}
