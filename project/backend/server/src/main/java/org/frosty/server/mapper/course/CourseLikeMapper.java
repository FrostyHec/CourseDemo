package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.frosty.server.entity.bo.CourseLike;

@Mapper
public interface CourseLikeMapper extends BaseMapper<CourseLike> {
}
