package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.mapper.course.ChapterMapper;
import org.frosty.server.mapper.course.CommentMapper;
import org.frosty.server.services.course.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    @Override
    public void addCommentToResource(Long id, ResourceComment comment) {

    }

    @Override
    public void addReplyToComment(Long id, ResourceComment reply) {

    }

    @Override
    public void updateComment(Long id, ResourceComment updatedComment) {

    }

    @Override
    public void deleteComment(Long id) {

    }

    @Override
    public ResourceComment findById(Long id) {
        return null;
    }

    @Override
    public List<ResourceComment> findAllByResourceId(Long id) {
        return List.of();
    }
}
