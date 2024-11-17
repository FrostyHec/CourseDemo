package org.frosty.server.test.controller.smoke_test;

import org.assertj.core.util.IterableUtil;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.CommentResource;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.comment.CommentAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@IdempotentControllerTest
public class ResourceCommentSmokeTest {
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
    @Autowired
    private ObjectStorageService oss;

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
        System.out.println("---------------");
        System.out.println(resourceComment1);
        System.out.println("---------------");

        assert Objects.equals(resourceComment1.getCommentText(), resourceComment.getCommentText());
        assert Objects.equals(resourceComment1.getUserId(), resourceComment.getUserId());


        // 3. Teacher replies to the comment
        ResourceComment resourceComment2 = commentAPI.getTemplateReplyComment(resourceId, 1L, uid1);
        commentAPI.createSuccess(token1, resourceId, resourceComment2);
        ResourceComment resourceComment3 = commentAPI.getSuccess(token, 2L);
        assert Objects.equals(resourceComment2.getCommentText(), resourceComment3.getCommentText());
        assert Objects.equals(resourceComment2.getUserId(), resourceComment3.getUserId());

        // 4. Student can get the comment (including the reply)
        List<CommentController.CommentWithUserAndFileAndAccessKey> comments = commentAPI.getAllSuccess(token, resourceId);
        assert comments.size() == 2;
        CommentController.CommentWithUserAndFileAndAccessKey stuRcvd, teaRcvd;
        if (comments.get(0).getUser().getUserId().equals(uid)) {
            stuRcvd = comments.get(0);
            teaRcvd = comments.get(1);
        } else {
            stuRcvd = comments.get(1);
            teaRcvd = comments.get(0);
        }
        assert Objects.equals(stuRcvd.getCommentText(), resourceComment.getCommentText());
        assert Objects.equals(teaRcvd.getCommentText(), resourceComment2.getCommentText());

        assert IterableUtil.isNullOrEmpty(stuRcvd.getCommentFiles());
        assert IterableUtil.isNullOrEmpty(teaRcvd.getCommentFiles());

        //5. student upload file for a comment
        var studentCommentId = stuRcvd.getCommentId();
        var commentResource = commentAPI.getTemplateCommentResourceMetadata(studentCommentId,"pdf");
        var file = resourceAPI.loadTemplateFile("test.pdf");
        commentAPI.uploadFilesSuccess(token, commentResource, studentCommentId, file);

        //6. can see the file
        comments = commentAPI.getAllSuccess(token, resourceId);
        CommentController.CommentWithUserAndFileAndAccessKey rcvdStu2=null;
        for (var e:comments){
            if(e.getCommentId().equals(studentCommentId)){
                rcvdStu2 = e;
                break;
            }
        }
        assert rcvdStu2!=null;
        var resourceWithAccessKey = CommonCheck.checkSingleAndGet(rcvdStu2.getCommentFiles());
        var rcvdResourceEntity = resourceWithAccessKey.getResourceEntity();
        commentAPI.checkCommentResourceMetadata(rcvdResourceEntity,commentResource);
        assert resourceWithAccessKey.getAccessKey()!=null;

        var fileId = rcvdResourceEntity.getId();
        var fileName = rcvdResourceEntity.getFileName();
        var rvcdFile = oss.get(fileName, byte[].class);
        assert Arrays.equals(rvcdFile, file.getBytes());

        // 7. can delete the file
        commentAPI.removeFilesSuccess(token, studentCommentId,fileId);

        comments = commentAPI.getAllSuccess(token, resourceId);
        for (var e:comments){
            if(e.getCommentId().equals(studentCommentId)){
                rcvdStu2 = e;
                break;
            }
        }
        assert IterableUtil.isNullOrEmpty(rcvdStu2.getCommentFiles());
    }
}