package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Resource;

import java.util.List;

@Mapper
public interface AssignmentMapper extends BaseMapper<Assignment> {
    @Select("SELECT * FROM assignments where chapter_id=#{chapterId}")
    List<Assignment> getAllByChapterId(Long chapterId);


    @Select("SELECT * FROM assignments where assignment_id =#{assId}")
    Assignment selectAssById(Long assId);


    @Update("UPDATE assignments SET " +
            "chapter_id = #{chapterId}, " +
            "description = #{description}, " +
            "assignment_type = #{assignmentType}, " +
            "allow_update_submission = #{allowUpdateSubmission}, " +
            "latest_submission_time = #{latestSubmissionTime}, " +
            "maximum_score = #{maximumScore}, " +
            "allow_student_to_view_score = #{allowStudentToViewScore} " +
            "WHERE assignment_id = #{assignmentId}")
    void updateAssById(Assignment assignment);


    @Delete("DELETE FROM assignments WHERE assignment_id = #{id}")
    void deleteAssById(Long id);

}
