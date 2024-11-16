package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.frosty.server.entity.bo.Enrollment;

import java.util.List;

@Mapper
public interface EnrollmentMapper extends BaseMapper<Enrollment> {
    @Insert("<script>" +
            "INSERT INTO enrollments (student_id, course_id, status, created_at, updated_at) " +
            "VALUES " +
            "<foreach collection='studentList' item='studentId' separator=','> " +
            "(#{studentId}, #{courseId}, 'invited', now(), now()) " +
            "</foreach> " +
            "ON CONFLICT (student_id, course_id) " +
            "DO UPDATE SET status = 'invited', updated_at = now()" +
            "</script>")
    void inviteStudentsToCourse(@Param("courseId") Long courseId, @Param("studentList") List<Long> studentList);
}
