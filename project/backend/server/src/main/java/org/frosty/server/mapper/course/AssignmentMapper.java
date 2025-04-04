package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.Assignment;

import java.util.List;

@Mapper
public interface AssignmentMapper extends BaseMapper<Assignment> {
    @Select("SELECT * FROM assignments where chapter_id=#{chapterId}")
    List<Assignment> getAllByChapterId(Long chapterId);


    @Select("SELECT * FROM assignments where assignment_id =#{assId}")
    Assignment selectAssById(Long assId);


//    @Update("UPDATE assignments SET " +
//            "chapter_id = #{chapterId}, " +
//            "description = #{description}, " +
//            "assignment_type = #{assignmentType}, " +
//            "allow_update_submission = #{allowUpdateSubmission}, " +
//            "latest_submission_time = #{latestSubmissionTime}, " +
//            "maximum_score = #{maximumScore}, " +
//            "allow_student_to_view_score = #{allowStudentToViewScore} " +
//            "WHERE assignment_id = #{assignmentId}")
//    void updateAssById(Assignment assignment);


    @Delete("DELETE FROM assignments WHERE assignment_id = #{id}")
    void deleteAssById(Long id);


    @Select("SELECT * FROM assignments where assignments.chapter_id =#{id}")
    List<Assignment> selectAssignmentsByChapterId(Long id);





    @Select("SELECT a.* FROM assignments a " +
            "JOIN chapters c ON a.chapter_id = c.chapter_id " +
            "WHERE c.course_id = #{cid}")
    List<Assignment> getAssignmentsByCourseId(Long cid);
}
