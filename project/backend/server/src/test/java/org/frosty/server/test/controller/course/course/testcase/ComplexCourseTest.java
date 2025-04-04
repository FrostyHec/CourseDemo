package org.frosty.server.test.controller.course.course.testcase;

import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@IdempotentControllerTest
public class ComplexCourseTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testMultipleCreate() throws Exception {
        var name = "test";
        var res = authAPI.quickAddUserAndLogin(name + "1", User.Role.teacher);
        var token1 = res.first;
        var uid1 = res.second.getUserId();
        res = authAPI.quickAddUserAndLogin(name + "2", User.Role.teacher);
        var token2 = res.first;
        var uid2 = res.second.getUserId();

        List<Course> createdList1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            var course = courseAPI.getTemplateCourse(uid1);
            course.setCourseName(i + "/" + uid1);
            createdList1.add(course);
            courseAPI.createSuccess(token1, course);
        }

        // check if multiple create successful
        var li = courseAPI.getAllTeachingCourseSuccess(token1, uid1);
        assert li.size() == 3;
        for (var e : createdList1) {
            var rcvdCourses = li.stream().filter(x -> x.getCourseName().equals(e.getCourseName())).toList();
            courseAPI.checkSingle(e, rcvdCourses, Course.CourseStatus.creating);
            li.remove(rcvdCourses.get(0));
        }

        // check if won't get other's course
        li = courseAPI.getAllTeachingCourseSuccess(token2, uid2);
        assert li.isEmpty();
    }

}
