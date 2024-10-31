package org.frosty.server.test.controller.course.file_submission;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.FileSubmissionController.FileSubmissionList;
import org.frosty.server.controller.course.FileSubmissionController.SubmissionScore;
import org.frosty.server.controller.course.FileSubmissionController.FileSubmissionWithAccessKey;
import org.frosty.server.entity.bo.FileSubmission;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@RequiredArgsConstructor
public class FileSubmissionAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String baseUrl = PathConstant.API + "/upload-submission";
    private final String assignmentBaseUrl = PathConstant.API +"/assignment";

    public FileSubmission getTemplateSubmission(Long assignmentId,
                                                Long studentId) {
        return new FileSubmission().setFileSubmissionId(1L)
                .setAssignmentId(assignmentId)
                .setStudentId(studentId)
                .setFileName("fuck");
    }
    public ResultActions submitFile(String token, Long assignmentId, FileSubmission fileSubmission, MultipartFile file) throws Exception {
        String url = assignmentBaseUrl+"/" + assignmentId + "/upload-submission";
        String json = objectMapper.writeValueAsString(fileSubmission);
        MockMultipartFile jsonFile = new MockMultipartFile("data", "", "application/json", json.getBytes());
        MockMultipartFile mockFile = new MockMultipartFile("file", file.getOriginalFilename(), file.getContentType(), file.getBytes());

        return mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(mockFile)
                .file(jsonFile)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void submitFileSuccess(String token, Long assignmentId, FileSubmission fileSubmission, MultipartFile file) throws Exception {
        submitFile(token, assignmentId, fileSubmission, file)
                .andExpect(status().isOk())
                .andExpect(RespChecker.success());
    }

    public ResultActions deleteFileSubmission(String token, Long id) throws Exception {
        String url = baseUrl + "/" + id;
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void deleteFileSubmissionSuccess(String token, Long id) throws Exception {
        deleteFileSubmission(token, id)
                .andExpect(status().isOk())
                .andExpect(RespChecker.success());
    }

    public ResultActions getFileSubmission(String token, Long id) throws Exception {
        String url = baseUrl + "/" + id;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public FileSubmissionWithAccessKey getFileSubmissionSuccess(String token, Long id) throws Exception {
        var resp = getFileSubmission(token, id)
                .andExpect(status().isOk())
                .andExpect(RespChecker.success())
                .andReturn();
        return objectMapper.readValue(resp.getResponse().getContentAsString(), FileSubmissionWithAccessKey.class);
    }
    public ResultActions updateScore(String token, Long id, SubmissionScore submissionScore) throws Exception {
        String url = baseUrl + "/" + id + "/score";
        String json = objectMapper.writeValueAsString(submissionScore);

        return mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateScoreSuccess(String token, Long id, SubmissionScore submissionScore) throws Exception {
        updateScore(token, id, submissionScore)
                .andExpect(status().isOk())
                .andExpect(RespChecker.success());
    }

    public ResultActions getStudentSubmission(String token, Long assignmentId) throws Exception {
        String url = assignmentBaseUrl + "/" + assignmentId + "/my-submission";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public FileSubmissionWithAccessKey getStudentSubmissionSuccess(String token, Long assignmentId) throws Exception {
        var resp = getStudentSubmission(token, assignmentId)
                .andExpect(status().isOk())
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, FileSubmissionWithAccessKey.class);
    }

    public ResultActions getAllSubmission(String token,Long assignmentId) throws Exception {
        String url = assignmentBaseUrl + "/" + assignmentId + "/all-submission";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<FileSubmission> getAllSubmissionSuccess(String token,Long assignmentId) throws Exception {
        var resp = getAllSubmission(token,assignmentId)
                .andExpect(status().isOk())
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, FileSubmissionList.class).getContent();
    }

}
