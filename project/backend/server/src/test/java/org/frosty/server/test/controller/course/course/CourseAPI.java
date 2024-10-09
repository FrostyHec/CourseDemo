package org.frosty.server.test.controller.course.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CourseController;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.mapper.course.CourseMapper;
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
public class CourseAPI {
    private final MockMvc mockMvc;
    private final CourseMapper mapper;
    private final ObjectMapper objectMapper;
    private final AuthAPI authAPI;
    private final String courseBaseUrl = PathConstant.API + "/course";
    public Course getTemplateCourse(Long teacherId) {
        return new Course()
                .setTeacherId(teacherId)
                .setCourseName("Course Name")
                .setDescription("Course Description")
                .setStatus(Course.CourseStatus.published)
                ;
    }

    public Long addTestCourseAndGetId(Long teacherId){
        return addTestCourse(teacherId).getCourseId();
    }

    public Course addTestCourse(Long teacherId){
        var entity = getTemplateCourse(teacherId);
        mapper.insert(entity);
        return entity;
    }
    public ResultActions adminGetRequiredApprovedCourse(String adminToken,Long userId,int pageSize,int pageNum) throws Exception {
        String url = PathConstant.API+"/admin/"+userId+"/courses/submitted";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authAPI.setAuthHeader(adminToken))
                        .param("page_num", String.valueOf(pageNum))
                        .param("page_size",String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Course> adminGetAllRequiredApprovedCourseSuccess(String adminToken, Long userId) throws Exception {
        var resp = adminGetRequiredApprovedCourse(adminToken,userId,1,-1)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseController.CourseList.class).getContent();
    }
    public ResultActions getAllTeachingCourse(String teacherToken,Long userId,int pageSize,int pageNum) throws Exception {
        String url = PathConstant.API+"/teacher/"+userId+"/courses";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authAPI.setAuthHeader(teacherToken))
                .param("page_num", String.valueOf(pageNum))
                .param("page_size",String.valueOf(pageSize))
                .accept(MediaType.APPLICATION_JSON));
    }
    public List<Course> getAllTeachingCourseSuccess(String teacherToken, Long userId) throws Exception {
        var resp = getAllTeachingCourse(teacherToken,userId,1,-1)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CourseController.CourseList.class).getContent();
    }
    public ResultActions create(String token, Course course) throws Exception {
        String json = objectMapper.writeValueAsString(course);
        return mockMvc.perform(MockMvcRequestBuilders.post(courseBaseUrl)
                .headers(authAPI.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }
    public void createSuccess(String teacherToken, Course course) throws Exception {
        create(teacherToken,course)
                .andExpect(RespChecker.success());
    }

    public ResultActions update(String token,Long cid, Course course) throws Exception {
        String json = objectMapper.writeValueAsString(course);
        return mockMvc.perform(MockMvcRequestBuilders.put(courseBaseUrl+"/"+cid)
                .headers(authAPI.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateSuccess(String teacherToken,Long cid,  Course course) throws Exception {
        update(teacherToken,cid,course)
                .andExpect(RespChecker.success());
    }

    public ResultActions updateStatus(String teacherToken, Long cid, Course.CourseStatus status) throws Exception {
        String json = objectMapper.writeValueAsString(Map.of("status",status));
        return mockMvc.perform(MockMvcRequestBuilders.put(courseBaseUrl+"/"+cid)
                .headers(authAPI.setAuthHeader(teacherToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }
    public void updateStatusSuccess(String teacherToken, Long courseId, Course.CourseStatus courseStatus) throws Exception {
        updateStatus(teacherToken,courseId,courseStatus)
                .andExpect(RespChecker.success());
    }

}
