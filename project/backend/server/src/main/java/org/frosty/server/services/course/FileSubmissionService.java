package org.frosty.server.services.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.controller.course.FileSubmissionController;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.FileSubmission;
import org.frosty.server.mapper.course.AssignmentMapper;
import org.frosty.server.mapper.course.FileSubmissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileSubmissionService {
    private final FileSubmissionMapper mapper;
    private final AssignmentMapper assignmentMapper;
    private final ObjectStorageService objectStorageService;

    private void throwIfNotAllowedMultipleSubmission(Long assignmentId){
        Assignment assignment = assignmentMapper.selectAssById(assignmentId);
        if(!assignment.getAllowUpdateSubmission()){ // if not allowed, throw exception
            throw new ExternalException(Response.getBadRequest("multiple-submission-not-allowed"));
        }
    }

    @Transactional
    public void submitFile(FileSubmission fileSubmission, MultipartFile file) throws IOException {
        // TODO ONLY STUDENT CAN SUBMIT

        // check if have previous submission, if does, check if multiple submission valid.
        Long assignmentId = fileSubmission.getAssignmentId(),studentId = fileSubmission.getStudentId();
        FileSubmission prevSubmission= mapper.selectSubmissionByAssignmentIdAndStudentId(assignmentId,studentId);
        if(prevSubmission!=null) { // has previous
            ///check if allowed multiple submission
            throwIfNotAllowedMultipleSubmission(assignmentId);
            // if allowed, remove old
            fileSubmission.setFileSubmissionId(prevSubmission.getFileSubmissionId());
            objectStorageService.delete(prevSubmission.getFileName());
        }
        fileSubmission.setFileName(
                UUID.randomUUID().toString() + file.getSize());
        mapper.insertOrUpdate(fileSubmission);
        objectStorageService.save(fileSubmission.getFileName(), file.getBytes());
    }
    @Transactional
    public void deleteFileSubmission(Long id) {
        // TODO ONLY STUDENT AND TEACHER CAN SUBMIT
        var fileSubmission = mapper.selectById(id);
        if(fileSubmission==null){ // 幂等性
            return;
        }
        throwIfNotAllowedMultipleSubmission(fileSubmission.getAssignmentId());
        var name = fileSubmission.getFileName();
        mapper.deleteById(id);
        objectStorageService.delete(name);
    }

    public FileSubmissionController.FileSubmissionWithAccessKey getFileSubmission(Long uid,Long id) {
        // TODO ONLY STUDENT AND TEACHER HAVE PRIVILEGE FOR ACCESSING
        FileSubmission fileSubmission = mapper.selectById(id);
        var accessKey = objectStorageService.getAccessKey(fileSubmission.getFileName(),
                getFileSubmissionCaseName(uid));
        return new FileSubmissionController.FileSubmissionWithAccessKey(fileSubmission, accessKey);
    }

    private String getFileSubmissionCaseName(Long uid) {
        return "file-submission-"+uid;
    }

    public void updateScore(Long id, Integer gainedScore) {
        // TODO ONLY TEACHER CAN UPDATE, AND CANNOT HIGHER THAN ASSIGNMENT SCORE MAXIMUM LIMITATION
        mapper.updateScoreById(id,gainedScore);
    }

    public FileSubmissionController.FileSubmissionWithAccessKey getStudentSubmission(long uid, Long assignmentId) {
        // TODO ONLY STUDENT CAN ACCESS
        FileSubmission fileSubmission =  mapper.selectSubmissionByAssignmentIdAndStudentId(assignmentId,uid);
        var accessKey = objectStorageService.getAccessKey(fileSubmission.getFileName(),
                getFileSubmissionCaseName(uid));
        return new FileSubmissionController.FileSubmissionWithAccessKey(fileSubmission, accessKey);
    }

    public List<FileSubmission> getAllSubmission(Long assignmentId) {
        // TODO ONLY TEACHER CAN ACCESS IT
        return mapper.getAllSubmission(assignmentId);
    }
}
