package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.AuthEx;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.utils.Ex;
import org.frosty.server.entity.bo.FileSubmission;
import org.frosty.server.services.course.FileSubmissionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class FileSubmissionController {
    private final FileSubmissionService fileSubmissionService;

    @PostMapping("/assignment/{id}/upload-submission")
    public void submitFile(@PathVariable Long id  ,
                           @RequestPart("data") FileSubmission fileSubmission,
                           @RequestPart("file") MultipartFile file) throws IOException {
        fileSubmission.setFileSubmissionId(null);
        fileSubmission.setGainedScore(null);
        Ex.idCheck(id,fileSubmission.getAssignmentId());
        fileSubmissionService.submitFile(fileSubmission, file);
    }

    @DeleteMapping("/upload-submission/{id}")
    public void deleteFileSubmission(@PathVariable Long id) {
        fileSubmissionService.deleteFileSubmission(id);
    }

    @GetMapping("/upload-submission/{id}")
    public FileSubmissionWithAccessKey getFileSubmission(@GetToken TokenInfo tokenInfo, @PathVariable Long id) {
        long uid=AuthEx.checkPass(tokenInfo).getUserID();
        return fileSubmissionService.getFileSubmission(uid,id);
    }
    @PatchMapping("/upload-submission/{id}/score")
    public void updateScore(@RequestBody SubmissionScore submissionScore, @PathVariable Long id) {
        fileSubmissionService.updateScore(id,submissionScore.getGainedScore());
    }
    @GetMapping("/assignment/{assignmentId}/my-submission")
    public FileSubmissionWithAccessKey getStudentSubmission(@GetToken TokenInfo tokenInfo, @PathVariable Long assignmentId) {
        long uid=AuthEx.checkPass(tokenInfo).getUserID();
        return fileSubmissionService.getStudentSubmission(uid,assignmentId);
    }
    @GetMapping("/assignment/{assignmentId}/all-submission")
    public FileSubmissionList getAllSubmission(@PathVariable Long assignmentId) {
        return new FileSubmissionList(fileSubmissionService.getAllSubmission(assignmentId));
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileSubmissionWithAccessKey {
        private FileSubmission fileSubmission;
        private String accessKey;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmissionScore {
        private Integer gainedScore;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileSubmissionList{
        private List<FileSubmission> content;
    }
}
