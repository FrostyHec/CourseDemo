package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.course_like.CourseLikeAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@IdempotentControllerTest
public class CourseLikeSmokeTest {
    // TODO 现在没办法过测，查看一下为什么
    // 这个异常通常表示在反序列化 JSON 数据时，Jackson 期望一个布尔值（Boolean），但是接收到的是一个对象（Object）。你需要检查你要反序列化的 JSON 数据格式和对应的 Java 类属性类型是否匹配。

    @Autowired
    private AuthAPI authAPI;

    @Autowired
    private CourseAPI courseAPI;

    @Autowired
    private CourseLikeAPI courseLikeAPI;

    @Test
    public void testCourseLikeCRUD() throws Exception {
        // Teacher login
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        // Create a course
        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Student login
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        // Student likes the course
        courseLikeAPI.createCourseLikeSuccess(studentToken, course.getCourseId(), student.getUserId());

        // Check if the student liked the course
        boolean isLiked = courseLikeAPI.checkCourseLikeSuccess(studentToken, course.getCourseId(), student.getUserId());
        assertTrue(isLiked);

        // Student unlikes the course
        courseLikeAPI.deleteCourseLikeSuccess(studentToken, course.getCourseId(), student.getUserId());

        // Check if the student unliked the course
        isLiked = courseLikeAPI.checkCourseLikeSuccess(studentToken, course.getCourseId(), student.getUserId());
        assertFalse(isLiked);
    }
}