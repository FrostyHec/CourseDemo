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

    @Select("SELECT * FROM Courses")
    List<Course> getAllCourses();

    @Select("<script>" +
            "SELECT c.* " +
            "FROM courses c " +
            "JOIN users u ON c.teacher_id = u.user_id " +
            "WHERE c.status = 'published' " +
            "AND c.publication = 'open' " +
            "AND (" +
            "c.course_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR u.first_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR u.last_name LIKE CONCAT('%', #{keyword}, '%')" +
            ") " +
            "<if test='pageSize != -1'>" +
            "LIMIT #{pageSize} OFFSET #{pageNum} * #{pageSize}" +
            "</if>" +
            "</script>")
    List<Course> searchPublicCourse(@Param("pageNum") int pageNum,
                                    @Param("pageSize") int pageSize,
                                    @Param("keyword") String keyword);
}

