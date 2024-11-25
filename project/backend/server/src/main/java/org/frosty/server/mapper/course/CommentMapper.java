package org.frosty.server.mapper.course;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.ResourceComment;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<ResourceComment> {
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
                rc.created_at AS createdAt,
                rc.updated_at AS updatedAt,
                u.user_id AS "user.userId",
                u.first_name AS "user.firstName",
                u.last_name AS "user.lastName",
                u.role AS "user.role",
                u.email AS "user.email"
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


    @Select("""
                SELECT * 
                FROM resource_comments
                WHERE user_id = #{userId} 
                  AND created_at >= #{createdAt.atStartOfDay()} 
                  AND created_at < #{createdAt.plusDays(1).atStartOfDay()}
            """)
    List<ResourceComment> selcetByUserIdAndCreatedTime(Long userId, OffsetDateTime createdAt);


    @Select("""
                SELECT c.course_id
                FROM resource_comments rc
                JOIN resources r ON rc.resource_id = r.resource_id
                JOIN chapters ch ON r.chapter_id = ch.chapter_id
                JOIN courses c ON ch.course_id = c.course_id
                WHERE rc.comment_id = #{commentId}
            """)
    Long findCourseIdByComment(@Param("commentId") Long commentId);
}
