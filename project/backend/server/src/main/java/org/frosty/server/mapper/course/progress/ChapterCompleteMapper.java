package org.frosty.server.mapper.course.progress;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.progress.ChapterCompleteRecord;
import org.frosty.server.entity.bo.progress.CourseCompleteRecord;

public interface ChapterCompleteMapper extends BaseMapper<ChapterCompleteRecord> {
    @Select("SELECT (SELECT true FROM chapter_complete_records WHERE chapter_id = #{chapterId} AND student_id = #{uid}) IS NOT NULL")
    boolean contains(Long chapterId, Long uid);

    @Delete("DELETE FROM chapter_complete_records WHERE chapter_id = #{cid}")
    void deleteAllByChapterId(Long cid);
}
