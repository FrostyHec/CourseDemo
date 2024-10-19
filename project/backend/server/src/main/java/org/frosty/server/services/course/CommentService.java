package org.frosty.server.services.course;

import org.frosty.server.entity.bo.ResourceComment;

import java.util.List;

public interface CommentService {
    void addCommentToResource(Long id, ResourceComment comment);

    void addReplyToComment(Long id, ResourceComment reply);

    void updateComment(Long id, ResourceComment updatedComment);

    void deleteComment(Long id);

    ResourceComment findById(Long id);

    List<ResourceComment> findAllByResourceId(Long id);
}
