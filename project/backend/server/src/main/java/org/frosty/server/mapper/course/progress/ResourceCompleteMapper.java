package org.frosty.server.mapper.course.progress;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.progress.ResourceCompleteRecord;

public interface ResourceCompleteMapper extends BaseMapper<ResourceCompleteRecord> {
    @Select("SELECT (SELECT true FROM resource_complete_records WHERE resource_id = #{resourceId} AND student_id = #{uid}) IS NOT NULL;")
    boolean contains(Long resourceId, long uid);
}
