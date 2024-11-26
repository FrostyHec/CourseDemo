package org.frosty.server.mapper.course.progress;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.controller.course.analysis.StudyAnalysisController;
import org.frosty.server.entity.bo.progress.ChapterCompleteRecord;
import org.frosty.server.entity.bo.progress.CourseCompleteRecord;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChapterCompleteMapper extends BaseMapper<ChapterCompleteRecord> {
    @Select("SELECT (SELECT true FROM chapter_complete_records WHERE chapter_id = #{chapterId} AND student_id = #{uid}) IS NOT NULL")
    boolean contains(Long chapterId, Long uid);

    @Delete("DELETE FROM chapter_complete_records WHERE chapter_id = #{cid}")
    void deleteAllByChapterId(Long cid);


    @Select("""
        <script>
        SELECT student_id, 
               CASE WHEN EXISTS (
                   SELECT 1 
                   FROM chapter_complete_records 
                   WHERE chapter_id = #{chapterId} 
                     AND student_id = t.student_id
               ) THEN true ELSE false END AS is_completed
        FROM (
            <foreach item='item' index='index' collection='uids' open='(' separator=' UNION ALL ' close=')'>
                SELECT #{item} AS student_id
            </foreach>
        ) t
        </script>
        """)
    List<Map<String, Object>> getCompletionStatuses(@Param("chapterId") Long chapterId, @Param("uids") List<Long> uids);

}
