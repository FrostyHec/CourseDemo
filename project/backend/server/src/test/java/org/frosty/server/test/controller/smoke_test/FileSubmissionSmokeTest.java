package org.frosty.server.test.controller.smoke_test;

import io.jsonwebtoken.lang.Assert;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.controller.course.FileSubmissionController;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.assignment.AssignmentAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.file_submission.FileSubmissionAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IdempotentControllerTest
public class FileSubmissionSmokeTest {
    @Autowired
    private FileSubmissionAPI api;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private AssignmentAPI assignmentAPI;
    @Autowired
    private ResourceAPI resourceAPI;
    @Autowired
    private ObjectStorageService oss;

    @Test
    public void testFileWithMultipleSubmission() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;
        var teacherId = teacher.getUserId();

        var chapterId = chapterAPI.addTestCourseTestChapterAndGetId(teacherId);

        var assignment = assignmentAPI.getTemplateAssignment(chapterId);
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment);
        assignment = assignmentAPI.getSuccess(teacherToken, chapterId);
        var assignmentId = assignment.getAssignmentId();

        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;
        var stuId = student.getUserId();
        // --- test start---
        // --- student create a submission
        var submission = api.getTemplateSubmission(assignmentId, student.getUserId());
        var file = resourceAPI.loadTemplateFile("test.pdf");
        api.submitFileSuccess(studentToken, assignmentId, submission, file);
        // the submission file can be got by student
        var rcvdSubmission = api.getStudentSubmissionSuccess(studentToken, assignmentId);
        var rcvdFileName = rcvdSubmission.getFileSubmission().getFileName();
        assert StringUtils.isNotBlank(rcvdSubmission.getAccessKey());
        var rvcdFile = oss.get(rcvdFileName, byte[].class);
        assert Arrays.equals(rvcdFile, file.getBytes());
        Long submissionId = rcvdSubmission.getFileSubmission().getFileSubmissionId();
        assert rcvdSubmission.getFileSubmission().getGainedScore() == null;
        // student can create new submission
        var submission2 = api.getTemplateSubmission(assignmentId, student.getUserId());
        var file2 = resourceAPI.loadTemplateFile("test.docx");
        api.submitFileSuccess(studentToken, assignmentId, submission2, file2);
        var rcvdSubmission2 = api.getStudentSubmissionSuccess(studentToken, assignmentId);
        var rcvdFileName2 = rcvdSubmission2.getFileSubmission().getFileName();
        assert StringUtils.isNotBlank(rcvdSubmission2.getAccessKey());
        assert !Objects.equals(rcvdFileName2, rcvdFileName);
        assert Objects.equals(submissionId,
                rcvdSubmission2.getFileSubmission().getFileSubmissionId());
        var rcvdFile2 = oss.get(rcvdFileName2, byte[].class);
        assert Arrays.equals(rcvdFile2, file2.getBytes());
        assert !Arrays.equals(rcvdFile2, rvcdFile);
        // teacher can get submission and update score
        var allSubmissions = api.getAllSubmissionSuccess(teacherToken, assignmentId);
        var teacherRcvdSubmission = CommonCheck.checkSingleAndGet(allSubmissions);
        assert Objects.equals(teacherRcvdSubmission.getFileSubmissionId(),
                submissionId);
        int score = 100;
        api.updateScoreSuccess(teacherToken, teacherRcvdSubmission.getFileSubmissionId(),
                new FileSubmissionController.SubmissionScore(100));
        // student can view score
        var rcvdSubmission3Metadata = api.getStudentSubmissionSuccess(studentToken, assignmentId)
                .getFileSubmission();
        assert Objects.equals(rcvdSubmission3Metadata.getFileSubmissionId(),
                submissionId);
        assert rcvdSubmission3Metadata.getGainedScore() == score;
    }
    @Test
    public void testFileRejectMultipleSubmission() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;
        var teacherId = teacher.getUserId();

        var chapterId = chapterAPI.addTestCourseTestChapterAndGetId(teacherId);

        var assignment = assignmentAPI.getTemplateAssignment(chapterId).setAllowUpdateSubmission(false);
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment);
        assignment = assignmentAPI.getSuccess(teacherToken, chapterId);
        var assignmentId = assignment.getAssignmentId();

        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;
        var stuId = student.getUserId();
        // --- test start ---
        var submission = api.getTemplateSubmission(assignmentId, student.getUserId());
        var file = resourceAPI.loadTemplateFile("test.pdf");
        api.submitFileSuccess(studentToken, assignmentId, submission, file);
        // after submit, student cannot delete or update
        var resp = api.submitFile(studentToken,assignmentId,submission,file)
                .andExpect(status().isOk())
                .andExpect(RespChecker.badRequest())
                .andReturn();
        assert "multiple-submission-not-allowed".equals(JsonUtils.getErrorString(resp));
        resp = api.deleteFileSubmission(studentToken,assignmentId)
                .andExpect(status().isOk())
                .andExpect(RespChecker.badRequest())
                .andReturn();
        assert "multiple-submission-not-allowed".equals(JsonUtils.getErrorString(resp));
        // TODO but teacher can delete (not implemented in code)
    }
}
