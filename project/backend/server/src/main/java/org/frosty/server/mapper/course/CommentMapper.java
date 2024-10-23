package org.frosty.server.mapper.course;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.ResourceComment;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<ResourceComment> {
//    // 给指定的资源添加一个评论
//    @Insert("")
//    void insertCommentToResource(Long resourceId, ResourceComment comment);
//
//
//    // 给指定的评论添加一个评论
//    @Insert("")
//    void insertReplyToComment(Long parentCommentId, ResourceComment reply);
//
//
//    // 更新指定评论
//    @Update("")
//    void updateCommentById(Long commentId, ResourceComment updatedComment);

    // 获取指定资源的全部评论
    @Select("SELECT * FROM resource_comments WHERE resource_id =#{resourceId}")
    List<ResourceComment> getAllByResourceId(Long resourceId);


    // TODO 确认一下返回的具体形式
    @Select("""
                SELECT 
                    rc.comment_id,
                    rc.resource_id,
                    rc.comment_text,
                    rc.comment_reply,
                    u.user_id,
                    u.first_name,
                    u.last_name,
                    u.email,
                    u.role
                FROM 
                    resource_comments rc
                JOIN 
                    users u 
                ON 
                    rc.user_id = u.user_id
                WHERE 
                    rc.resource_id = #{resourceId}
            """)
    List<CommentWithUser> getAllByResourceId(@Param("resourceId") int resourceId);

}
