package org.frosty.server.mapper.course;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.controller.course.CommentController;
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
                rc.comment_id AS commentId,
                rc.resource_id AS resourceId,
                rc.comment_text AS commentText,
                rc.comment_reply AS commentReply,
                u.user_id AS "userPublicInfo.userId",
                u.first_name AS "userPublicInfo.firstName",
                u.last_name AS "userPublicInfo.lastName",
                u.role AS "userPublicInfo.role",
                u.email AS "userPublicInfo.email"
            FROM
                resource_comments rc
            JOIN
                users u
            ON
                rc.user_id = u.user_id
            WHERE
                rc.resource_id = #{resourceId}
            """)
//    @Select("SELECT * FROM resource_comments WHERE resource_id = #{resourceId}")
    List<CommentController.CommentWithUser> getAllPublicByResourceId(@Param("resourceId") long resourceId);

}
