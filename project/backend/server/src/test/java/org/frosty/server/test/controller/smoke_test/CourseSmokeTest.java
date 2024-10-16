package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
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
        var li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        courseAPI.checkSingle(course, li, Course.CourseStatus.creating);
        var cid = li.get(0).getCourseId();

        course.setCourseId(cid);

        // 2. update info
        course.setCourseName("new " + course.getCourseName());
        courseAPI.updateSuccess(teacherToken, cid, course);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        courseAPI.checkSingle(course, li, Course.CourseStatus.creating);


        // submit course
        courseAPI.updateStatusSuccess(teacherToken, course.getCourseId(), Course.CourseStatus.submitted);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        courseAPI.checkSingle(course, li, Course.CourseStatus.submitted);

        // approve course
        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        var pCourse = CommonCheck.checkSingleAndGet(li);
        courseAPI.checkSingle(course, pCourse, Course.CourseStatus.submitted);
        courseAPI.updateStatusSuccess(adminToken, course.getCourseId(), Course.CourseStatus.published);

        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        assert li.isEmpty();

        //see approved
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        courseAPI.checkSingle(course, li, Course.CourseStatus.published);
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
        courseAPI.checkSingle(course, li, Course.CourseStatus.creating);
        var cid = li.get(0).getCourseId();

        course.setCourseId(1L); // set course id to 1 to test idempotent

        // 2. update info
        course.setCourseName("new " + course.getCourseName());
        courseAPI.updateSuccess(teacherToken, cid, course);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        courseAPI.checkSingle(course, li, Course.CourseStatus.creating);

        // submit course
        courseAPI.updateStatusSuccess(teacherToken, course.getCourseId(), Course.CourseStatus.submitted);
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        courseAPI.checkSingle(course, li, Course.CourseStatus.submitted);

        // approve course
        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        var pCourse = CommonCheck.checkSingleAndGet(li);
        courseAPI.checkSingle(course, pCourse, Course.CourseStatus.submitted);
        courseAPI.updateStatusSuccess(adminToken, course.getCourseId(), Course.CourseStatus.rejected);

        li = courseAPI.adminGetAllRequiredApprovedCourseSuccess(adminToken, admin.getUserId());
        assert li.isEmpty();

        //see rejected
        li = courseAPI.getAllTeachingCourseSuccess(teacherToken, teacher.getUserId());
        courseAPI.checkSingle(course, li, Course.CourseStatus.rejected);
    }
}
