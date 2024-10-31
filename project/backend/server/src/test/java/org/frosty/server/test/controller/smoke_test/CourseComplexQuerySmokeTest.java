package org.frosty.server.test.controller.smoke_test;

import org.frosty.server.controller.course.CourseComplexQueryController;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.assignment.AssignmentAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.comment.CommentAPI;
import org.frosty.server.test.controller.course.complexQuery.ComplexQueryAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class CourseComplexQuerySmokeTest {
    // TODO check if can query the course metadata, all chapter metadata, all content metadata(in different types).
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private ResourceAPI resourceAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private AssignmentAPI assignmentAPI;

    @Autowired
    private ComplexQueryAPI complexQueryAPI;

    @Test
    public void testGetCourseAllStructureInfo() throws Exception {
        var name = "teacher";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.teacher);
        var token = res.first;
        var uid = res.second.getUserId();
        var resourceId = resourceAPI.addTestCourseTestChapterTestResourceAndGetId(uid);
        var resourceId1 = resourceAPI.addTestCourseTestChapterTestResourceAndGetId(uid);
        var assId = assignmentAPI.addTestCourseTestChapterTestAssAndGetId(uid);
        var assId1 = assignmentAPI.addTestCourseTestChapterTestAssAndGetId(uid);
        Long courseId = 1L; // 替换为有效的课程 ID

        var chapters = chapterAPI.getAllSuccess(token,1L);
        System.out.println("---------------------------");
        System.out.println(chapters);
        System.out.println("---------------------------");

        CourseComplexQueryController.StructureInfo structureInfo = complexQueryAPI.getCourseAllStructureInfoSuccess(token, courseId);
        System.out.println("---------------------------");
        System.out.println(structureInfo);
        System.out.println("---------------------------");
    }

}
