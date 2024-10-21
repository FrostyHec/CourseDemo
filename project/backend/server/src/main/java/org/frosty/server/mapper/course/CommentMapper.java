package org.frosty.server.mapper.course;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.frosty.server.entity.bo.ResourceComment;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<ResourceComment> {
    // 给指定的资源添加一个评论
    void insertCommentToResource(Long resourceId, ResourceComment comment);


    // 给指定的评论添加一个评论
    void insertReplyToComment(Long parentCommentId, ResourceComment reply);


    // 更新指定评论
    void updateCommentById(Long commentId, ResourceComment updatedComment);

    List<ResourceComment> getAllByResourceId(Long resourceId);
}
