package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.Enrollment;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.course_member.CourseMemberAPI;
import org.frosty.server.test.controller.user.UserAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Slf4j
@IdempotentControllerTest
public class CourseMemberSmokeTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private CourseMemberAPI courseMemberAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private UserAPI userAPI;

    @Test
    public void testStudentJoinFromInvitation() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "test", Course.PublicationType.open);

        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        courseAPI.quickCreateCourse(course);

//---test start---
// 0. teacher find the student
        var foundStudent = userAPI.getUserSuccess(teacherToken, student.getUserId());
        assert foundStudent.getUserId().equals(student.getUserId());

// 1. teacher invite student
        courseMemberAPI.inviteStudentsToCourseSuccess(teacherToken, course.getCourseId(), List.of(student.getUserId()));

// 2. student find itself joined
        var studentStatus = courseMemberAPI.getStudentStatusSuccess(studentToken, course.getCourseId(), student.getUserId());
        assert studentStatus == Enrollment.EnrollmentType.invited;

// 3. teacher find student joined
        var studentListAfterInvite = courseMemberAPI.getStudentListSuccess(teacherToken, course.getCourseId(), 1, -1);
        var invitedStudent = CommonCheck.checkSingleAndGet(studentListAfterInvite);
        assert invitedStudent.getStudent().getUserId().equals(student.getUserId());
        assert invitedStudent.getStatus() == Enrollment.EnrollmentType.invited;

    }

    @Test
    public void testStudentActiveJoin() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "test", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);
        var closedCourse = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "test2", Course.PublicationType.closed);
        courseAPI.quickCreateCourse(closedCourse);
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        //---test start---
        // 0. student find the course
        var received = courseAPI.searchPublicCourseSuccess(
                studentToken, 0, -1, "es");
        courseAPI.checkSingle(course, received, Course.CourseStatus.published);
        var cid = CommonCheck.checkSingleAndGet(received).getCourseId();
        // 1. student join open course
        courseMemberAPI.enrollStudentToCourse(studentToken, cid);


        // 2. student find itself joined
        var status = courseMemberAPI.getStudentStatusSuccess(studentToken, cid, student.getUserId());
        assert status == Enrollment.EnrollmentType.publik;

        // 3. teacher find student joined
        var rcvdStuList = courseMemberAPI.getStudentListSuccess(teacherToken, cid, 1, -1);
        var receiveStudent = CommonCheck.checkSingleAndGet(rcvdStuList);
        //assert receiveStudent.getStatus() == Enrollment.EnrollmentType.publik;
        userAPI.checkPublicUserEquality(student, receiveStudent.getStudent());

        // 4. teacher changed student to invited
        courseMemberAPI.inviteStudentsToCourse(teacherToken, cid, List.of(student.getUserId()));

        // 5. student find itself invited
        var studentCourses = courseMemberAPI.getStudentCoursesSuccess(studentToken, student.getUserId(), 1, -1);
        var studentCourseId = CommonCheck.checkSingleAndGet(studentCourses).getCourseId();
        assert Objects.equals(studentCourseId, cid);
    }

    @Test
    public void testGetTeacherCourses() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "test", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Get teacher courses
        var teacherCourses = courseMemberAPI.getTeacherCoursesSuccess(teacherToken, teacher.getUserId(), 1, -1);
        var createdCourse = CommonCheck.checkSingleAndGet(teacherCourses);
        assert createdCourse.getCourseId().equals(course.getCourseId());
    }

    @Test
    public void testGetSubmittedCourses() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("admin", User.Role.admin);
        var adminToken = pair.first;
        var admin = pair.second;

        var course = courseAPI.getTemplatePublishedCourse(
                admin.getUserId(), "test", Course.PublicationType.open);
        course.setStatus(Course.CourseStatus.submitted);
        courseAPI.quickCreateCourse(course);

        // Get submitted courses
        var submittedCourses = courseMemberAPI.getSubmittedCoursesSuccess(adminToken, admin.getUserId(), 1, -1);
        var createdCourse = CommonCheck.checkSingleAndGet(submittedCourses);
        assert createdCourse.getCourseId().equals(course.getCourseId());
    }
}
