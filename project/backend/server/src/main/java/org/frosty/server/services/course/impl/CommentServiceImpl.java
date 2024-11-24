package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.CommentResource;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;
import org.frosty.server.mapper.course.CommentMapper;
import org.frosty.server.mapper.course.CommentResourceMapper;
import org.frosty.server.services.course.CommentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentResourceMapper commentResourceMapper;
    private final ObjectStorageService objectStorageService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void addCommentToResource(Long resourceId, ResourceComment comment) {
        commentMapper.insert(comment);
        applicationEventPublisher.publishEvent(new CreateCommentEvent(this, comment.getCommentId()));
    }

    @Override
    public void addReplyToComment(Long parentCommentId, ResourceComment reply) {
        commentMapper.insert(reply);
        applicationEventPublisher.publishEvent(new CreateCommentEvent(this, reply.getCommentId()));
    }

    @Override
    public void updateComment(Long commentId, ResourceComment updatedComment) {
        // BaseMapper中的updateById.
        commentMapper.updateById(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentMapper.deleteById(commentId);
    }

    @Override
    public ResourceComment findById(Long commentId) {
        return commentMapper.selectById(commentId);
    }

    @Override
    public List<CommentController.CommentWithUserAndFileAndAccessKey> findAllByResourceId(AuthInfo auth, Long resourceId) {
        long uid = auth.getUserID();
        var comments = commentMapper.getAllPublicByResourceId(resourceId);
        List<CommentController.CommentWithUserAndFileAndAccessKey> result = new ArrayList<>(comments.size());
        for (var comment : comments) {
            var cid = comment.getCommentId();
            List<CommentResource> resources = commentResourceMapper.getAllByCommentId(cid);
            List<CommentController.CommentResourceWithAccessKey> resourceWithAccessKeys = new ArrayList<>(resources.size());
            for (var resource : resources) {
                var accessKey = objectStorageService.getAccessKey(getCommentFileCaseName(uid), resource.getFileName());
                resourceWithAccessKeys.add(new CommentController.CommentResourceWithAccessKey(resource, accessKey));
            }
            result.add(new CommentController.CommentWithUserAndFileAndAccessKey(comment, resourceWithAccessKeys));
        }
        return result;
    }

    @Override
    @Transactional
    public void uploadFileForComment(CommentResource commentResource, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + file.getSize() + "." + commentResource.getSuffix();
        fileName = fileName.replace("/", "."); // TODO 防止注入漏洞
        commentResource.setFileName(fileName);
        commentResourceMapper.insert(commentResource);
        objectStorageService.save(commentResource.getFileName(), file.getBytes());
    }

    private String getCommentFileCaseName(long cid) {
        return "comment-resource-" + cid;
    }

    @Override
    @Transactional
    public void removeFiles(Long cid, Long fid) {
        // may check sth
        CommentResource commentResource = commentResourceMapper.selectById(fid);
        commentResourceMapper.deleteById(fid);
        objectStorageService.delete(commentResource.getFileName());
    }

    @Override
    public List<ResourceComment> findByUserIdAndCreatedTime(Long userId, OffsetDateTime createdAt) {
        return commentMapper.selcetByUserIdAndCreatedTime(userId, createdAt);
    }
}
