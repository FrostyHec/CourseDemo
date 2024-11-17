package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.CommentResource;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.mapper.course.CommentMapper;
import org.frosty.server.mapper.course.CommentResourceMapper;
import org.frosty.server.services.course.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentResourceMapper commentResourceMapper;
    private final ObjectStorageService objectStorageService;

    @Override
    public void addCommentToResource(Long resourceId, ResourceComment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void addReplyToComment(Long parentCommentId, ResourceComment reply) {
        commentMapper.insert(reply);
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
    public List<CommentController.CommentWithUser> findAllByResourceId(Long resourceId) {
        return commentMapper.getAllPublicByResourceId(resourceId);
    }

    @Override
    @Transactional
    public void uploadFileForComment(CommentResource commentResource, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + file.getSize() + "." + commentResource.getSuffix();
        fileName = fileName.replace("/","."); // TODO 防止注入漏洞
        commentResource.setFileName(fileName);
        commentResourceMapper.insert(commentResource);
        objectStorageService.save(commentResource.getFileName(), file.getBytes());
    }

    @Override
    @Transactional
    public void removeFiles(Long cid, String fid) {
        // may check sth
        commentResourceMapper.deleteById(fid);
        objectStorageService.delete(fid);
    }
}
