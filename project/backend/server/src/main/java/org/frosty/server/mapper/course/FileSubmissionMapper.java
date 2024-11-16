package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.server.entity.bo.FileSubmission;

import java.util.List;

public interface FileSubmissionMapper extends BaseMapper<FileSubmission> {
    @Select("select * from file_submission where assignment_id = #{assignmentId} and student_id = #{studentId}")
    FileSubmission selectSubmissionByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
    @Update("update file_submission set gained_score = #{gainedScore} where file_submission_id = #{id}")
    void updateScoreById(Long id, Integer gainedScore);

    @Select("select * from file_submission where assignment_id = #{assignmentId}")
    List<FileSubmission> getAllSubmission(Long assignmentId);
}
