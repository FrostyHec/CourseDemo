package org.frosty.server.test.controller.course.progress;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.progress.CourseProgressController;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Component
@RequiredArgsConstructor
public class CourseProgressAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String baseUrl = PathConstant.API;

    public ResultActions completeResource(String token, Long rid) throws Exception {
        String url = baseUrl + "/resource/" + rid + "/study/complete";
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void completeResourceSuccess(String token, Long rid) throws Exception {
        completeResource(token, rid)
                .andExpect(RespChecker.success());
    }

    public ResultActions completeChapter(String token, Long cid) throws Exception {
        String url = baseUrl + "/chapter/" + cid + "/study/complete";
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void completeChapterSuccess(String token, Long cid) throws Exception {
        completeChapter(token, cid)
                .andExpect(RespChecker.success());
    }

    public ResultActions completeCourse(String token, Long csid) throws Exception {
        String url = baseUrl + "/course/" + csid + "/study/complete";
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void completeCourseSuccess(String token, Long csid) throws Exception {
        completeCourse(token, csid)
                .andExpect(RespChecker.success());
    }

    public ResultActions checkCourseProgress(String token, Long csid) throws Exception {
        String url = baseUrl + "/course/" + csid + "/study";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public CourseProgressController.CourseProgress checkCourseProgressSuccess(String token, Long csid) throws Exception {
        var resp = checkCourseProgress(token, csid)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        return JsonUtils.toObject(resp, CourseProgressController.CourseProgress.class);
    }

    public ResultActions clearAllStudentCourseProgress(String token, Long csid) throws Exception {
        String url = baseUrl + "/course/" + csid + "/study/all-clear";
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void clearAllStudentCourseProgressSuccess(String token, Long csid) throws Exception {
        clearAllStudentCourseProgress(token, csid)
                .andExpect(RespChecker.success());
    }

    public ResultActions clearAllStudentChapterProgress(String token, Long cid) throws Exception {
        String url = baseUrl + "/chapter/" + cid + "/study/all-clear";
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void clearAllStudentChapterProgressSuccess(String token, Long cid) throws Exception {
        clearAllStudentChapterProgress(token, cid)
                .andExpect(RespChecker.success());
    }

    public ResultActions clearAllStudentResourceProgress(String token, Long rid) throws Exception {
        String url = baseUrl + "/resource/" + rid + "/study/all-clear";
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void clearAllStudentResourceProgressSuccess(String token, Long rid) throws Exception {
        clearAllStudentResourceProgress(token, rid)
                .andExpect(RespChecker.success());
    }
}