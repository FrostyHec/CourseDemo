package org.frosty.server.test.controller.smoke_test;

import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.course_member.CourseMemberAPI;
import org.frosty.server.test.controller.course.livestream.LiveStreamAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IdempotentControllerTest
public class LiveStreamAuthSmokeTest {
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private CourseMemberAPI courseMemberAPI;
    @Autowired
    private LiveStreamAPI liveStreamAPI;
    @Test
    public void testCanPassAuthCheck() throws Exception {
        // init a teacher and a student of a course
        var mockUserName = "test";
        var res = authAPI.quickAddUserAndLogin(mockUserName+"-teacher", User.Role.teacher);
        var teacherToken = res.first;
        var teacherUid = res.second.getUserId();
        var courseId = courseAPI.addTestCourseAndGetId(teacherUid);

        res = authAPI.quickAddUserAndLogin(mockUserName+"-student", User.Role.student);
        var stuToken = res.first;
        var stuId = res.second.getUserId();
        courseMemberAPI.inviteStudentsToCourseSuccess(teacherToken,courseId, List.of(stuId));

        // teacher get stream
        String teacherRcvdName = liveStreamAPI.getPushNameSuccess(teacherToken,courseId).getName();
        assert teacherRcvdName!=null;
        liveStreamAPI.getPushAuthSuccess(teacherRcvdName);

        // student can see
        String stuRcvdName = liveStreamAPI.getPullNameSuccess(stuToken,courseId).getName();
        assert stuRcvdName!=null;
        assert teacherRcvdName.equals(stuRcvdName);
        liveStreamAPI.getPullAuthSuccess(stuToken,stuRcvdName);
    }
}
