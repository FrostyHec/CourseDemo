package org.frosty.server.test.controller.course.chapter.testcase;

import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class ComplexChapterTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testGetCorrectness() throws Exception {
        // TODO 对于学生不得返回invisible，入课状态为public的不得返回non-public
    }
}
