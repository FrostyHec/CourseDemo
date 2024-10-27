package org.frosty.server.test.controller.smoke_test;

import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class ChapterSmokeTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private AuthAPI authAPI;
    @Test
    public void testBasicCRUD() throws Exception {
        var name = "test";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.teacher);
        var token = res.first;
        var uid = res.second.getUserId();
        var courseId = courseAPI.addTestCourseAndGetId(uid);

        //-----test start---
        Chapter chapter = chapterAPI.getTemplateTeachingChapter();
        String title = chapter.getChapterTitle();
        chapterAPI.createSuccess(token, courseId, chapter);

        var li = chapterAPI.getAllSuccess(token, courseId);
        assert li.size() == 1;

        var rcvdChapter = li.get(0);
        assert rcvdChapter.getChapterTitle().equals(title);

        var id = rcvdChapter.getChapterId();
        rcvdChapter.setChapterTitle("new " + title);
        chapterAPI.updateSuccess(token, id, rcvdChapter);

        var rcvdChapter2 = chapterAPI.getSuccess(token, id);
        assert rcvdChapter2.getChapterTitle().equals("new " + title);
    }
}
