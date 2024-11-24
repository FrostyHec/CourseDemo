package org.frosty.server.services.course;

import org.frosty.auth.entity.AuthInfo;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.CommentResource;
import org.frosty.server.entity.bo.ResourceComment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

public interface CommentService {
    void addCommentToResource(Long id, ResourceComment comment);

    void addReplyToComment(Long id, ResourceComment reply);

    void updateComment(Long id, ResourceComment updatedComment);

    void deleteComment(Long id);

    ResourceComment findById(Long id);

    List<CommentController.CommentWithUserAndFileAndAccessKey> findAllByResourceId(AuthInfo auth,Long id);

    void uploadFileForComment(CommentResource commentResource, MultipartFile file) throws IOException;

    void removeFiles(Long cid, Long fid);

    List<ResourceComment> findByUserIdAndCreatedTime(Long userId, OffsetDateTime createdAt);
}
