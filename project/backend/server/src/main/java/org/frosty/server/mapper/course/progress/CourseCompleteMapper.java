package org.frosty.server.mapper.course.progress;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.progress.CourseCompleteRecord;

public interface CourseCompleteMapper extends BaseMapper<CourseCompleteRecord> {
    @Delete("DELETE FROM course_complete_records WHERE course_id = #{csid}")
    void deleteAllByCourseId(Long csid);
    @Select("SELECT (SELECT true FROM course_complete_records WHERE course_id = #{csid} AND student_id = #{uid}) IS NOT NULL")
    boolean contains(Long csid, long uid);
}
