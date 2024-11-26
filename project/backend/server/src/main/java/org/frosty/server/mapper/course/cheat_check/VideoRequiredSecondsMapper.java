package org.frosty.server.mapper.course.cheat_check;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.cheat_check.VideoRequiredSeconds;

@Mapper
public interface VideoRequiredSecondsMapper extends BaseMapper<VideoRequiredSeconds> {
    @Select("with required_seconds_table as (" +
            "           select required_seconds>0 from video_required_seconds where video_id = #{rid}" +
            "    ) select count(*)>0 from required_seconds_table;")
    boolean hasWatchRequirement(Long rid);
}
