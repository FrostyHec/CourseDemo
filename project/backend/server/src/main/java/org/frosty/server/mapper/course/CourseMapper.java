package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.controller.course.RecommendController;
import org.frosty.server.entity.bo.Course;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

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

    @Select("""
            <script>
            SELECT c.course_id AS courseId, c.course_name AS courseName, c.description AS description, 
                   c.teacher_id AS teacherId, c.status AS status, c.publication AS publication, 
                   COUNT(e.student_id) AS studentNum
            FROM courses c
            LEFT JOIN enrollments e ON c.course_id = e.course_id
            WHERE c.status = 'published'
              AND (c.publication = 'open' OR c.publication = 'semi_open')
            GROUP BY c.course_id
            ORDER BY studentNum DESC
            <if test='pageSize != -1'>
            LIMIT #{pageSize} OFFSET #{pageNum} * #{pageSize}
            </if>
            </script>
            """)
    @Results({
            @Result(property = "course.courseId", column = "courseId"),
            @Result(property = "course.courseName", column = "courseName"),
            @Result(property = "course.description", column = "description"),
            @Result(property = "course.teacherId", column = "teacherId"),
            @Result(property = "course.status", column = "status"),
            @Result(property = "course.publication", column = "publication"),
            @Result(property = "studentNum", column = "studentNum")
    })
    List<RecommendController.CourseWithStudentCount> getHotCourses(@Param("pageNum") int pageNum,
                                                                   @Param("pageSize") int pageSize);

    @Select("SELECT * FROM Courses WHERE teacher_id = #{teacherId} LIMIT #{pageSize} OFFSET (#{pageNum} -1)* #{pageSize}")
    List<Course> selectTeacherCourses(Long teacherId, int pageNum, int pageSize);

    @Select("SELECT * FROM Courses WHERE status = 'rejected' or status = 'published' LIMIT #{pageSize} OFFSET (#{pageNum} -1)* #{pageSize}")
    List<Course> selectHandledCourse(int pageNum, int pageSize);
}

