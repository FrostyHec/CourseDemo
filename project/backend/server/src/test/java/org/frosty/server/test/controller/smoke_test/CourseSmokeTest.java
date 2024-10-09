package org.frosty.server.test.controller.smoke_test;

import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IdempotentControllerTest
public class CourseSmokeTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private AuthAPI authAPI;
    @Test
    public void testApproveFlow() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        pair = authAPI.quickAddUserAndLogin("admin", User.Role.admin);
        var adminToken = pair.first;
        var admin = pair.second;
        var course = courseAPI.getTemplateCourse(teacher.getUserId());
        //---test start---
        // 1. create and fetch
        courseAPI.createSuccess(teacherToken, course);
        System.out.println("course :" + course);
        var li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.created);
        var cid = li.get(0).getCourseId();

        course.setCourseId(cid); // set course id to 1 to test idempotent

        // 2. update info
        course.setCourseName("new " + course.getCourseName());
        courseAPI.updateSuccess(teacherToken, cid, course);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.created);


        // submit course
        courseAPI.updateStatusSuccess(teacherToken, course.getCourseId(), Course.CourseStatus.submitted);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.submitted);

        // approve course
        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        var pCourse = CommonCheck.checkAndGetOne(li);
        checkSingle(course, pCourse, Course.CourseStatus.submitted);
        courseAPI.updateStatusSuccess(adminToken, course.getCourseId(), Course.CourseStatus.approved);

        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        assert li.isEmpty();

        //see approved
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.approved);
    }

    @Test
    public void testRejectFlow() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        pair = authAPI.quickAddUserAndLogin("admin", User.Role.admin);
        var adminToken = pair.first;
        var admin = pair.second;
        var course = courseAPI.getTemplateCourse(teacher.getUserId());
        //---test start---
        // 1. create and fetch
        courseAPI.createSuccess(teacherToken, course);
        var li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.created);
        var cid = li.get(0).getCourseId();

        course.setCourseId(1L); // set course id to 1 to test idempotent

        // 2. update info
        course.setCourseName("new " + course.getCourseName());
        courseAPI.updateSuccess(teacherToken, cid, course);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.created);

        // submit course
        courseAPI.updateStatusSuccess(teacherToken, course.getCourseId(), Course.CourseStatus.submitted);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.submitted);

        // approve course
        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        var pCourse = CommonCheck.checkAndGetOne(li);
        checkSingle(course, pCourse, Course.CourseStatus.submitted);
        courseAPI.updateStatusSuccess(adminToken, course.getCourseId(), Course.CourseStatus.rejected);

        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        assert li.isEmpty();

        //see rejected
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        checkSingle(course, li, Course.CourseStatus.rejected);
    }

    private void checkSingle(Course origin, List<Course> rcvdLi, Course.CourseStatus targetStatus) {
        var rcvdCourse = CommonCheck.checkAndGetOne(rcvdLi);
        checkSingle(origin, rcvdCourse, targetStatus);
    }

    private void checkSingle(Course origin, Course rcvdCourse, Course.CourseStatus targetStatus) {
        assert rcvdCourse.getCourseName().equals(origin.getCourseName());
        assert rcvdCourse.getDescription().equals(origin.getDescription());
        assert rcvdCourse.getTeacherId().equals(origin.getTeacherId());
        assert rcvdCourse.getStatus().equals(targetStatus);
    }

}
