package org.frosty.server.services.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.controller.course.FileSubmissionController;
import org.frosty.server.entity.bo.FileSubmission;
import org.frosty.server.mapper.course.FileSubmissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileSubmissionService {
    private final FileSubmissionMapper mapper;
    private final ObjectStorageService objectStorageService;

    @Transactional
    public void submitFile(FileSubmission fileSubmission, MultipartFile file) throws IOException {
        fileSubmission.setFileName(
                UUID.randomUUID().toString() + file.getSize());
        mapper.insert(fileSubmission);
        objectStorageService.save(fileSubmission.getFileName(),file.getBytes());
    }
    @Transactional
    public void deleteFileSubmission(Long id) {
        var fileSubmission = mapper.selectById(id);
        var name = fileSubmission.getFileName();
        mapper.deleteById(id);
        objectStorageService.delete(name);
    }

    public FileSubmissionController.FileSubmissionWithAccessKey getFileSubmission(Long uid,Long id) {
        FileSubmission fileSubmission = mapper.selectById(id);
        var accessKey = objectStorageService.getAccessKey(fileSubmission.getFileName(),
                getFileSubmissionCaseName(uid));
        return new FileSubmissionController.FileSubmissionWithAccessKey(fileSubmission, accessKey);
    }

    private String getFileSubmissionCaseName(Long uid) {
        return "file-submission-"+uid;
    }
}
