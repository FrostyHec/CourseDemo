package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.CommentResource;

import java.util.List;

@Mapper
public interface CommentResourceMapper extends BaseMapper<CommentResource> {
    @Select("SELECT * FROM comment_resources WHERE comment_id = #{cid}")
    List<CommentResource> getAllByCommentId(Long cid);
}
