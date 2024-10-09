package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.Course;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

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