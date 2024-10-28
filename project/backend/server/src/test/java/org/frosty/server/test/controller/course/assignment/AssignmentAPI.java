package org.frosty.server.test.controller.course.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.mapper.course.AssignmentMapper;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;


@Component
@RequiredArgsConstructor
public class AssignmentAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String assignmentBaseUrl = PathConstant.API + "/assignment";
    private final String chapterBaseUrl = PathConstant.API + "/chapter";
    private final ChapterAPI chapterAPI;
    private final AssignmentMapper assignmentMapper;

    public Assignment getTemplateAssignment(Long chapterId) {
        return new Assignment()
                .setChapterId(chapterId)
                .setDescription("AssignmentDescription")
                .setAssignmentType(Assignment.AssignmentType.online_form)
                .setAllowUpdateSubmission(Boolean.TRUE)
                .setLatestSubmissionTime(OffsetDateTime.now().plusDays(7))
                .setMaximumScore(100)
                .setAllowStudentToViewScore(Boolean.TRUE);
    }

    public ResultActions create(String token, Long chapterId, Assignment assignment) throws Exception {
        String url = chapterBaseUrl + "/" + chapterId + "/assignment";
        String json = objectMapper.writeValueAsString(assignment);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void createSuccess(String token, Long chapterId, Assignment assignment) throws Exception {
        create(token, chapterId, assignment)
                .andExpect(RespChecker.success());
    }

    public ResultActions update(String token, Long assignmentId, Assignment assignment) throws Exception {
        String url = assignmentBaseUrl + "/" + assignmentId;
        String json = objectMapper.writeValueAsString(assignment);
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateSuccess(String token, Long assignmentId, Assignment assignment) throws Exception {
        update(token, assignmentId, assignment)
                .andExpect(RespChecker.success());
    }

    public ResultActions delete(String token, Long assignmentId) throws Exception {
        String url = assignmentBaseUrl + "/" + assignmentId;
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void deleteSuccess(String token, Long assignmentId) throws Exception {
        delete(token, assignmentId)
                .andExpect(RespChecker.success());
    }

    public ResultActions get(String token, Long assignmentId) throws Exception {
        String url = assignmentBaseUrl + "/" + assignmentId;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public Assignment getSuccess(String token, Long assignmentId) throws Exception {
        var resp = get(token, assignmentId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, Assignment.class);
    }

    public Long addTestCourseTestChapterTestAssAndGetId(Long uid){
        var chapterId = chapterAPI.addTestCourseTestChapterAndGetId(uid);
        var e = getTemplateAssignment(chapterId);
        assignmentMapper.insert(e);
        assert e.getAssignmentId() != null;
        return e.getAssignmentId();
    }
}