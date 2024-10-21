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
        // TODO check basic join in flow  from invited
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(),"test", Course.PublicationType.open);

        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        //---test start---
        // 0. teacher find the student
//        var rcvd =courseAPI.search(
//                studentToken,0,-1,"es");
//        courseAPI.checkSingle(course,rcvd, Course.CourseStatus.published);
//        var cid = CommonCheck.checkSingleAndGet(rcvd).getCourseId();
        // 1. teacher invite student
//        courseMemberAPI.enrollStudentToCourse(studentToken,cid);


        // 2. student find itself joined
//        var status =courseMemberAPI.getStudentStatusSuccess(studentToken,cid, student.getUserId());
//        assert status == Enrollment.EnrollmentType.publik;

        // 3. teacher find student joined
//        var rcvdStuList = courseMemberAPI.getStudentListSuccess(teacherToken,cid,1,-1);
//        var rcvdStu = CommonCheck.checkSingleAndGet(rcvdStuList);
//        assert rcvdStu.getStatus() == Enrollment.EnrollmentType.publik;
//        userAPI.checkPublicUserEquality(student,rcvdStu.getStudent());
    }

    @Test
    public void testStudentActiveJoin() throws Exception {
        // TODO mention to check student wont seached out the non-public course.
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(),"test", Course.PublicationType.open);

        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        //---test start---
        // 0. student find the course
        var rcvd =courseAPI.searchPublicCourseSuccess(
                studentToken,0,-1,"es");
        courseAPI.checkSingle(course,rcvd, Course.CourseStatus.published);
        var cid = CommonCheck.checkSingleAndGet(rcvd).getCourseId();
        // 1. student join open course
        courseMemberAPI.enrollStudentToCourse(studentToken,cid);


        // 2. student find itself joined
        var status =courseMemberAPI.getStudentStatusSuccess(studentToken,cid, student.getUserId());
        assert status == Enrollment.EnrollmentType.publik;

        // 3. teacher find student joined
        var rcvdStuList = courseMemberAPI.getStudentListSuccess(teacherToken,cid,1,-1);
        var rcvdStu = CommonCheck.checkSingleAndGet(rcvdStuList);
        assert rcvdStu.getStatus() == Enrollment.EnrollmentType.publik;
        userAPI.checkPublicUserEquality(student,rcvdStu.getStudent());

        // 4. teacher changed student to invited

        // 5. student find itself invited

    }
}
