package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.Course;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    @Insert("INSERT INTO Courses (course_name, description, teacher_id, status, publication) VALUES (#{courseName}, #{description}, #{teacherId}, #{status},#{publication} )")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    void insertCourse(Course course);

    @Update("UPDATE Courses SET status = #{status} WHERE course_id = #{courseId}")
    void updateCourseStatus(@Param("courseId") Long courseId, @Param("status") String status);

    @Update("UPDATE Courses SET course_name = #{courseName}, description = #{description} WHERE course_id = #{courseId}")
    void updateCourse(@Param("courseId") Long courseId, @Param("courseName") String name, @Param("description") String description);

    @Select("SELECT * FROM Courses WHERE course_id = #{courseId}")
    Course getCourse(@Param("courseId") Long courseId);

    @Delete("DELETE FROM Courses WHERE course_id = #{courseId}")
    void deleteCourse(@Param("courseId") Long courseId);

    @Select("SELECT * FROM Courses")
    List<Course> getAllCourses();

    @Select("SELECT * FROM Courses WHERE teacher_id = #{teacherId}")
    List<Course> findCoursesByTeacherId(Long teacherId);

    @Select("SELECT * FROM Courses WHERE status = 'submitted'")
    List<Course> adminGetRequiredApprovedCourse(Long adminId);
}