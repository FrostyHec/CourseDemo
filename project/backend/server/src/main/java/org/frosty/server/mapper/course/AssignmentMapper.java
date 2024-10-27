package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Resource;

import java.util.List;

@Mapper
public interface AssignmentMapper extends BaseMapper<Assignment> {
    @Select("SELECT * FROM assignments where chapter_id=#{chapterId}")
    List<Assignment> getAllByChapterId(Long chapterId);

}
