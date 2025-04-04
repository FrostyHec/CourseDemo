package org.frosty.server.mapper.course.progress;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.progress.ResourceCompleteRecord;

import java.util.List;

@Mapper
public interface ResourceCompleteMapper extends BaseMapper<ResourceCompleteRecord> {
    @Select("SELECT (SELECT true FROM resource_complete_records WHERE resource_id = #{resourceId} AND student_id = #{uid}) IS NOT NULL;")
    boolean contains(Long resourceId, long uid);

    @Delete("DELETE FROM resource_complete_records WHERE resource_id = #{rid}")
    void deleteAllByResourceId(Long rid);

    @Select("""
            <script>
            SELECT * FROM resource_complete_records WHERE resource_id IN 
            <foreach item='resourceId' collection='resourceIds' open='(' separator=',' close=')'>
            #{resourceId}
            </foreach> AND student_id IN 
            <foreach item='studentId' collection='studentIds' open='(' separator=',' close=')'>
            #{studentId}
            </foreach>
            </script>
            """
    )
    List<ResourceCompleteRecord> getCompletionStatuses(@Param("resourceIds") List<Long> resourceIds, @Param("studentIds") List<Long> studentIds);


}