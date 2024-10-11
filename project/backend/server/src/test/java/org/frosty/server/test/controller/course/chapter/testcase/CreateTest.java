package org.frosty.server.test.controller.course.chapter.testcase;

import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class CreateTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testCreate() throws Exception {
        var name = "test";
        var token = authAPI.quickAddUserAndLogin(name, User.Role.teacher);
//        var courseId = courseAPI.addTestCourse(token,name); // TODO wait for hlh
//        chapterAPI.createSuccess(token,courseId,chapterAPI.templateTeachingChapter);
    }
}
