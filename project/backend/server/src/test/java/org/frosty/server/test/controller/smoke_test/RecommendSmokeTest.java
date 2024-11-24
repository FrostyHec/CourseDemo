package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.User;
import org.frosty.server.controller.course.RecommendController.TeacherWithStudentCount;
import org.frosty.server.controller.course.RecommendController.CourseWithStudentCount;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.course_member.CourseMemberAPI;
import org.frosty.server.test.controller.course.recommend.RecommendAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@IdempotentControllerTest
public class RecommendSmokeTest {

    @Autowired
    private CourseAPI courseAPI;

    @Autowired
    private CourseMemberAPI courseMemberAPI;

    @Autowired
    private RecommendAPI recommendAPI;

    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testGetHotCourses() throws Exception {
        // Teacher login
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
//        var teacherToken = pair.first;
        var teacher = pair.second;

        // Create a course
        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Student login
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
//        var student = pair.second;

        // Search for the course
        var courseId = courseAPI.
                searchPublicCourseSuccess(studentToken, 1, -1, "testCourse")
                .get(0).getCourseId();
        course.setCourseId(courseId);

        // enroll in the course
        courseMemberAPI.enrollStudentToCourse(studentToken, courseId);

        // Get hot courses
        var hotCourses = recommendAPI.getHotCoursesSuccess(studentToken, 1, 1);
        assertFalse(hotCourses.isEmpty());
        System.out.println("1: "+hotCourses.getClass().getName());
        System.out.println("2: "+hotCourses.get("content").getClass().getName());
        assert(hotCourses.get("content").get(0) instanceof CourseWithStudentCount);
        System.out.println("3: "+hotCourses.get("content").get(0).getClass().getName());
        System.out.println("4: "+hotCourses.get("content").get(0).getCourse());
        System.out.println("5: "+hotCourses.get("content").get(0).getCourse().getCourseName());
        assertEquals("testCourse", hotCourses.get("content").get(0).getCourse().getCourseName());
    }

    @Test
    public void testGetHotTeachers() throws Exception {
        // Teacher login
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
//        var teacherToken = pair.first;
        var teacher = pair.second;

        // Create a course
        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Student login
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
//        var student = pair.second;

        var courseId = courseAPI.
                searchPublicCourseSuccess(studentToken, 1, -1, "testCourse")
                .get(0).getCourseId();
        course.setCourseId(courseId);

        // enroll in the course
        courseMemberAPI.enrollStudentToCourse(studentToken, courseId);

        // Get hot teachers
        var hotTeachers = recommendAPI.getHotTeachersSuccess(studentToken, 1, 1);
        assertFalse(hotTeachers.isEmpty());
        assertEquals(teacher.getUserId(), hotTeachers.get("content").get(0).getTeacher().getUserId());
    }
}