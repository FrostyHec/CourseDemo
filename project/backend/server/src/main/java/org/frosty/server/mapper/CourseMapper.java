package org.frosty.server.mapper;

import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.po.Course;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Insert("INSERT INTO Courses (name, description, status) VALUES (#{name}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertCourse(Course course);

    @Update("UPDATE Courses SET status = #{status} WHERE id = #{id}")
    void updateCourseStatus(@Param("id") Long id, @Param("status") String status);

    @Update("UPDATE Courses SET name = #{name}, description = #{description} WHERE id = #{id}")
    void updateCourse(@Param("id") Long id, @Param("name") String name, @Param("description") String description);

    @Select("SELECT * FROM Courses WHERE id = #{id}")
    Course getCourse(@Param("id") Long id);

    @Delete("DELETE FROM Courses WHERE id = #{id}")
    void deleteCourse(@Param("id") Long id);

    @Select("SELECT * FROM Courses")
    List<Course> getAllCourses();
}