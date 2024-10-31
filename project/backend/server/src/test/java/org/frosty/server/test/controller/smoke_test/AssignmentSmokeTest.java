package org.frosty.server.test.controller.smoke_test;

import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.assignment.AssignmentAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class AssignmentSmokeTest {
    // TODO 现在没办法过测，查看一下为什么
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private AssignmentAPI assignmentAPI;
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testBasicCRUD() throws Exception {
        var name = "testTeacher";
        var studentName = "testStudent";

        // 教师登录并创建课程
        var teacherRes = authAPI.quickAddUserAndLogin(name, User.Role.teacher);
        var teacherToken = teacherRes.first;
        var teacher = teacherRes.second;


        // 学生登录
        var studentRes = authAPI.quickAddUserAndLogin(studentName, User.Role.student);
        var studentToken = studentRes.first;
        var student = studentRes.second;

        var chapterId = chapterAPI.addTestCourseTestChapterAndGetId(teacher.getUserId());
        System.out.println("-------------------");
        System.out.println(chapterId);
        System.out.println("-------------------");
        // 1. 教师创建作业
        Assignment assignment = assignmentAPI.getTemplateAssignment(chapterId);
        var chapter = chapterAPI.getSuccess(teacherToken,1L);

        System.out.println("-------------------");
        System.out.println(chapter);
        System.out.println("-------------------");
        System.out.println(assignment);
        System.out.println("-------------------");
        String description = assignment.getDescription();
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment);

        // 验证创建的作业
        var createdAssignment = assignmentAPI.getSuccess(teacherToken, 1L);
        assert createdAssignment.getDescription().equals(description);

        // 2. 教师更新作业
        createdAssignment.setDescription("Updated " + description);
        assignmentAPI.updateSuccess(teacherToken, createdAssignment.getAssignmentId(), createdAssignment);

        // 验证更新的作业
        var updatedAssignment = assignmentAPI.getSuccess(teacherToken, createdAssignment.getAssignmentId());
        assert updatedAssignment.getDescription().equals("Updated " + description);

        // 3. 学生获取作业
        var fetchedAssignmentByStudent = assignmentAPI.getSuccess(studentToken, createdAssignment.getAssignmentId());
        assert fetchedAssignmentByStudent.getDescription().equals("Updated " + description);

        // 4. 教师删除作业
        assignmentAPI.deleteSuccess(teacherToken, createdAssignment.getAssignmentId());

        // 验证删除
        var allAssignmentsAfterDelete = assignmentAPI.getSuccess(teacherToken, chapterId);
        assert allAssignmentsAfterDelete == null;
    }
}
