package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.server.entity.bo.FileSubmission;

import java.util.List;

@Mapper
public interface FileSubmissionMapper extends BaseMapper<FileSubmission> {
    @Select("select * from file_submission where assignment_id = #{assignmentId} and student_id = #{studentId}")
    FileSubmission selectSubmissionByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    @Update("update file_submission set gained_score = #{gainedScore} where file_submission_id = #{id}")
    void updateScoreById(Long id, Integer gainedScore);

    @Select("select * from file_submission where assignment_id = #{assignmentId}")
    List<FileSubmission> getAllSubmission(Long assignmentId);

    @Select("SELECT COUNT(1) > 0 FROM file_submission WHERE assignment_id = #{assignmentId} AND student_id = #{userId}")
    boolean getSubmissionByAssignmentIdAndStudentId(Long assignmentId, Long userId);

    @Select("SELECT AVG(gained_score) FROM file_submission WHERE assignment_id = #{assignmentId} and gained_score is not null")
    double getAverageScoreByAssignmentId(Long assignmentId);

    @Select("SELECT fs.* FROM file_submission fs JOIN assignments a ON fs.assignment_id = a.assignment_id WHERE a.chapter_id = #{cpid}")
    List<FileSubmission> getAllSubmissionByChapterId(Long cpid);
}
