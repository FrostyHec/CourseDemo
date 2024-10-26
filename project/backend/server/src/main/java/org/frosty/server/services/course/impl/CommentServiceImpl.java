package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.mapper.course.CommentMapper;
import org.frosty.server.services.course.CommentService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

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
}
