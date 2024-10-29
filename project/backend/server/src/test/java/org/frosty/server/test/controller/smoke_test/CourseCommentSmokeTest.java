package org.frosty.server.test.controller.smoke_test;

import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.comment.CommentAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IdempotentControllerTest
public class CourseCommentSmokeTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private CommentAPI commentAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private ResourceAPI resourceAPI;
    @Autowired
    private ChapterAPI chapterAPI;

    @Test
    public void testCourseCommentFlow() throws Exception {
        var name = "student";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token = res.first;
        var uid = res.second.getUserId();
        var resourceId = resourceAPI.addTestCourseTestChapterTestResourceAndGetId(uid);
        var name1 = "teacher";
        var res1 = authAPI.quickAddUserAndLogin(name1, User.Role.teacher);
        var token1 = res1.first;
        var uid1 = res1.second.getUserId();


        //-----test start---
        // 1. Student creates a comment under the resource
        ResourceComment resourceComment = commentAPI.getTemplateComment(resourceId, uid);
        commentAPI.createSuccess(token, resourceId, resourceComment);

        // 2. Teacher can get the comment
        ResourceComment resourceComment1 = commentAPI.getSuccess(token1, 1L);
        System.out.println("----------------------------");
        System.out.println(resourceComment1);
        System.out.println("----------------------------");
//        assert commentId1 == commentId;

        // 3. Teacher replies to the comment
        ResourceComment resourceComment2 = commentAPI.getTemplateReplyComment(resourceId, 1L, uid1);
        commentAPI.createSuccess(token1, resourceId, resourceComment2);

        System.out.println("----------------------------");
        System.out.println(resourceComment2);
        System.out.println("----------------------------");

        // 4. Student can get the comment (including the reply)
        List<CommentController.CommentWithUser> comments = commentAPI.getAllSuccess(token, resourceId);

        System.out.println("----------------------------");
        System.out.println(comments);
        System.out.println("----------------------------");

        assert comments.size() == 2;

    }
}