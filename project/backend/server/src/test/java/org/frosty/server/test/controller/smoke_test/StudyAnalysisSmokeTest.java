package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.controller.course.FileSubmissionController;
import org.frosty.server.controller.course.analysis.StudyAnalysisController;
import org.frosty.server.controller.course.analysis.StudyAnalysisController.*;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.assignment.AssignmentAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.course_member.CourseMemberAPI;
import org.frosty.server.test.controller.course.file_submission.FileSubmissionAPI;
import org.frosty.server.test.controller.course.progress.CourseProgressAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.server.test.controller.course.study_analysis.StudyAnalysisAPI;
import org.frosty.server.test.controller.user.UserAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@IdempotentControllerTest
public class StudyAnalysisSmokeTest {
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private CourseMemberAPI courseMemberAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private UserAPI userAPI;
    @Autowired
    private StudyAnalysisAPI studyAnalysisAPI;
    @Autowired
    private FileSubmissionAPI fileSubmissionAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private AssignmentAPI assignmentAPI;
    @Autowired
    private CourseProgressAPI courseProgressAPI;
    @Autowired
    private ResourceAPI resourceAPI;

    @Test
    public void testGetAllStudentsWarning() throws Exception {
        // Create a teacher and login
        var teacherRes = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = teacherRes.first;
        var teacherId = teacherRes.second.getUserId();

        // Create a course
        var course = courseAPI.getTemplateCourse(teacherId);
        courseAPI.quickCreateCourse(course);

        // Create a chapter
        var chapterId = chapterAPI.addTestChapterAndGetId(course.getCourseId());

        // Create three students and login
        var student1Res = authAPI.quickAddUserAndLogin("student1", User.Role.student);
        var student1Token = student1Res.first;
        var student1Id = student1Res.second.getUserId();

        var student2Res = authAPI.quickAddUserAndLogin("student2", User.Role.student);
        var student2Token = student2Res.first;
        var student2Id = student2Res.second.getUserId();

        var student3Res = authAPI.quickAddUserAndLogin("student3", User.Role.student);
        var student3Token = student3Res.first;
        var student3Id = student3Res.second.getUserId();

        // Invite three students
        courseMemberAPI.inviteStudentsToCourseSuccess(teacherToken, course.getCourseId(), List.of(student1Id, student2Id, student3Id));
        var StudentList = courseMemberAPI.getStudentListSuccess(teacherToken, course.getCourseId(), 1, -1);
        assert StudentList.size() == 3;

        // Create an assignment
        var assignment = assignmentAPI.getTemplateAssignment(chapterId);
        assignment.setAssignmentName("Assignment");
        assignment.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        // var assignmentId = assignment.getAssignmentId();
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment);
        var assignmentId = 1L; // TODO

        // Student 1 submits the assignment with 0 score
        var submission1 = fileSubmissionAPI.getTemplateSubmission(assignmentId, student1Id);
        fileSubmissionAPI.submitFileSuccess(student1Token, assignmentId, submission1, new MockMultipartFile
                ("file", "test1.pdf", "application/pdf", new byte[0]));
        // Student 2 submits the assignment with 100 score
        var submission2 = fileSubmissionAPI.getTemplateSubmission(assignmentId, student2Id);
        fileSubmissionAPI.submitFileSuccess(student2Token, assignmentId, submission2, new MockMultipartFile
                ("file", "test2.pdf", "application/pdf", new byte[0]));

        // Verify the submissions
        var submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId);
        assert submissions.size() == 2;

        // give grades
        fileSubmissionAPI.updateScoreSuccess(teacherToken, submissions.get(0).getFileSubmissionId(),
                new FileSubmissionController.SubmissionScore(0));
        fileSubmissionAPI.updateScoreSuccess(teacherToken, submissions.get(1).getFileSubmissionId(),
                new FileSubmissionController.SubmissionScore(100));

        submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId);
        var sub1 = submissions.stream().filter(s -> s.getStudentId().equals(student1Id)).findFirst().orElse(null);
        var sub2 = submissions.stream().filter(s -> s.getStudentId().equals(student2Id)).findFirst().orElse(null);

        assert sub1 != null && sub1.getGainedScore() == 0;
        assert sub2 != null && sub2.getGainedScore() == 100;

        // Create a study analysis
        var studyAnalysis = studyAnalysisAPI.getAllStudentsWarningSuccess(teacherToken, course.getCourseId());
        List<StudyAnalysisController.WarningInfo> studentWarnings = studyAnalysis.getContent();
        assert studentWarnings.size() == 2;
        StudyAnalysisController.WarningInfo studentWarning1 = studentWarnings.get(1);
        StudyAnalysisController.WarningInfo studentWarning2 = studentWarnings.get(0);

        // check the result
        assert studentWarning1.getWarningType() == StudyAnalysisController.WarningInfo.WarningType.low_score &&
                studentWarning1.getWarningStudent().getUserId().equals(student1Id);
        assert studentWarning2.getWarningType() == StudyAnalysisController.WarningInfo.WarningType.homework_uncompleted &&
                studentWarning2.getWarningStudent().getUserId().equals(student3Id);
    }

    @Test
    public void testGetMyWarnings() throws Exception {
        // Create a teacher and login
        var teacherRes = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = teacherRes.first;
        var teacherId = teacherRes.second.getUserId();

        // Create a course
        var course = courseAPI.getTemplateCourse(teacherId);
        courseAPI.quickCreateCourse(course);

        // Create a chapter
        var chapterId = chapterAPI.addTestChapterAndGetId(course.getCourseId());

        // Create the student and login
        var student1Res = authAPI.quickAddUserAndLogin("student1", User.Role.student);
        var student1Token = student1Res.first;
        var student1Id = student1Res.second.getUserId();
        // create a high score student
        var student2Res = authAPI.quickAddUserAndLogin("student2", User.Role.student);
        var student2Token = student2Res.first;
        var student2Id = student2Res.second.getUserId();

        // Invite the student
        courseMemberAPI.inviteStudentsToCourseSuccess(teacherToken, course.getCourseId(), List.of(student1Id, student2Id));
        var StudentList = courseMemberAPI.getStudentListSuccess(teacherToken, course.getCourseId(), 1, -1);
        assert StudentList.size() == 2;

        // Create 3 assignments
        var assignment1 = assignmentAPI.getTemplateAssignment(chapterId);
        assignment1.setAssignmentName("Assignment1");
        assignment1.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment1);
        var assignmentId1 = 1L; // hard code
        var assignment2 = assignmentAPI.getTemplateAssignment(chapterId);
        assignment2.setAssignmentName("Assignment2");
        assignment2.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment2);
        var assignmentId2 = 2L; // hard code
        var assignment3 = assignmentAPI.getTemplateAssignment(chapterId);
        assignment3.setAssignmentName("Assignment3");
        assignment3.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment3);
        var assignmentId3 = 3L; // hard code

        // Student submits the 2 of assignments
        var submission1 = fileSubmissionAPI.getTemplateSubmission(assignmentId1, student1Id);
        fileSubmissionAPI.submitFileSuccess(student1Token, assignmentId1, submission1, new MockMultipartFile
                ("file", "test1.pdf", "application/pdf", new byte[0]));
        var submission2 = fileSubmissionAPI.getTemplateSubmission(assignmentId2, student1Id);
        fileSubmissionAPI.submitFileSuccess(student1Token, assignmentId2, submission2, new MockMultipartFile
                ("file", "test2.pdf", "application/pdf", new byte[0]));
        var submission3 = fileSubmissionAPI.getTemplateSubmission(assignmentId1, student2Id);
        fileSubmissionAPI.submitFileSuccess(student2Token, assignmentId1, submission3, new MockMultipartFile
                ("file", "test3.pdf", "application/pdf", new byte[0]));

        // teacher grades the assignments
        var submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId1);
        fileSubmissionAPI.updateScoreSuccess(teacherToken, submissions.get(0).getFileSubmissionId(),
                new FileSubmissionController.SubmissionScore(0));
        submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId2);
        fileSubmissionAPI.updateScoreSuccess(teacherToken, submissions.get(0).getFileSubmissionId(),
                new FileSubmissionController.SubmissionScore(100));
        submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId1);
        fileSubmissionAPI.updateScoreSuccess(teacherToken, submissions.get(1).getFileSubmissionId(),
                new FileSubmissionController.SubmissionScore(100));

        // Create a study analysis
        var studyAnalysis = studyAnalysisAPI.getMyWarningSuccess(student1Token, course.getCourseId());
        List<StudyAnalysisController.WarningInfo> studentWarnings = studyAnalysis.getContent();
        assert studentWarnings.size() == 2;

        // check the result
        StudyAnalysisController.WarningInfo studentWarning1 = studentWarnings.get(0);
        StudyAnalysisController.WarningInfo studentWarning2 = studentWarnings.get(1);
        assert studentWarning1.getWarningType() == StudyAnalysisController.WarningInfo.WarningType.low_score &&
                studentWarning1.getDescription() instanceof StudyAnalysisController.AssignmentContent &&
                ((StudyAnalysisController.AssignmentContent)
                        studentWarning1.getDescription()).getAssignmentEntity().getAssignmentId().equals(assignmentId1);
        assert studentWarning2.getWarningType() == StudyAnalysisController.WarningInfo.WarningType.homework_uncompleted &&
                studentWarning2.getDescription() instanceof StudyAnalysisController.AssignmentContent &&
                ((StudyAnalysisController.AssignmentContent)
                        studentWarning2.getDescription()).getAssignmentEntity().getAssignmentId().equals(assignmentId3);
    }

    @Test
    public void testGetAllStudentsScore() throws Exception {
        // 1 teacher , 1 course, 1 chapter, 3 assignments, 3 students, 9 submissions
        // 1 teacher
        var teacherRes = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = teacherRes.first;
        var teacherId = teacherRes.second.getUserId();

        // 1 course
        var course = courseAPI.getTemplateCourse(teacherId);
        courseAPI.quickCreateCourse(course);

        // 1 chapter
        var chapterId = chapterAPI.addTestChapterAndGetId(course.getCourseId());

        // 3 students
        var student1Res = authAPI.quickAddUserAndLogin("student1", User.Role.student);
        var student1Token = student1Res.first;
        var student1Id = student1Res.second.getUserId();
        var student2Res = authAPI.quickAddUserAndLogin("student2", User.Role.student);
        var student2Token = student2Res.first;
        var student2Id = student2Res.second.getUserId();
        var student3Res = authAPI.quickAddUserAndLogin("student3", User.Role.student);
        var student3Token = student3Res.first;
        var student3Id = student3Res.second.getUserId();

        // invite students
        courseMemberAPI.inviteStudentsToCourseSuccess(teacherToken, course.getCourseId(), List.of(student1Id, student2Id, student3Id));
        var StudentList = courseMemberAPI.getStudentListSuccess(teacherToken, course.getCourseId(), 1, -1);
        assert StudentList.size() == 3;

        // 3 assignments
        var assignment1 = assignmentAPI.getTemplateAssignment(chapterId);
        assignment1.setAssignmentName("Assignment1");
        assignment1.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment1);
        var assignmentId1 = 1L; // hard code
        var assignment2 = assignmentAPI.getTemplateAssignment(chapterId);
        assignment2.setAssignmentName("Assignment2");
        assignment2.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment2);
        var assignmentId2 = 2L; // hard code
        var assignment3 = assignmentAPI.getTemplateAssignment(chapterId);
        assignment3.setAssignmentName("Assignment3");
        assignment3.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment3);
        var assignmentId3 = 3L; // hard code

        // 9 submissions
        for (int i = 0; i < 3; i++) {
            // i is the assignment index
            for (int j = 0; j < 3; j++) {
                var studentToke = switch (j) {
                    case 0 -> student1Token;
                    case 1 -> student2Token;
                    case 2 -> student3Token;
                    default -> throw new IllegalStateException("Unexpected value: " + j);
                };
                // student submit assignment
                var submission = fileSubmissionAPI.getTemplateSubmission(assignmentId1 + i, student1Id + j);
                fileSubmissionAPI.submitFileSuccess(studentToke, assignmentId1 + i, submission, new MockMultipartFile
                        ("file", "test" + (i * 3 + j) + ".pdf", "application/pdf", new byte[0]));

                // teacher grades the assignment
                var submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId1 + i);
                fileSubmissionAPI.updateScoreSuccess(teacherToken, submissions.get(j).getFileSubmissionId(),
                        new FileSubmissionController.SubmissionScore((i + 1) * 30 + (j) * 3));
            }
            // student 1 gets 30, 60, 90
            // student 2 gets 33, 63, 93
            // student 3 gets 36, 66, 96
        }

        // create a study analysis
        var studyAnalysis = studyAnalysisAPI.getAllStudentsScoreSuccess(teacherToken, course.getCourseId());
        var studentsScoreTable = studyAnalysis.getAverageScore();
        assert studyAnalysis.getAverageScore().size() == 3;
        assert Objects.equals(studentsScoreTable, List.of(33, 63, 93));
        assert studyAnalysis.getTotalScore().size() == 3;
        assert studyAnalysis.getTotalScore().equals(List.of(180, 189, 198));
        assert studyAnalysis.getRow().size() == 3;
        assert studyAnalysis.getData().equals(List.of(List.of(30, 33, 36), List.of(60, 63, 66), List.of(90, 93, 96)));
    }

    @Test
    public void testGetMyScore() throws Exception {
        // 1 teacher , 1 course, 1 chapter, 3 assignments, 3 students, 9 submissions
        // 1 teacher
        var teacherRes = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = teacherRes.first;
        var teacherId = teacherRes.second.getUserId();

        // 1 course
        var course = courseAPI.getTemplateCourse(teacherId);
        courseAPI.quickCreateCourse(course);

        // 3 chapters
        var chapterId = chapterAPI.addTestChapterAndGetId(course.getCourseId());
        var chapterId2 = chapterAPI.addTestChapterAndGetId(course.getCourseId());
        var chapterId3 = chapterAPI.addTestChapterAndGetId(course.getCourseId());

        // 1 student
        var student1Res = authAPI.quickAddUserAndLogin("student1", User.Role.student);
        var student1Token = student1Res.first;
        var student1Id = student1Res.second.getUserId();

        // invite students
        courseMemberAPI.inviteStudentsToCourseSuccess(teacherToken, course.getCourseId(), List.of(student1Id));
        var StudentList = courseMemberAPI.getStudentListSuccess(teacherToken, course.getCourseId(), 1, -1);
        assert StudentList.size() == 1;

        // 3 assignments
        var assignment1 = assignmentAPI.getTemplateAssignment(chapterId);
        assignment1.setAssignmentName("Assignment1");
        assignment1.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId, assignment1);
        var assignmentId1 = 1L; // hard code
        var assignment2 = assignmentAPI.getTemplateAssignment(chapterId2);
        assignment2.setAssignmentName("Assignment2");
        assignment2.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId2, assignment2);
        var assignmentId2 = 2L; // hard code
        var assignment3 = assignmentAPI.getTemplateAssignment(chapterId3);
        assignment3.setAssignmentName("Assignment3");
        assignment3.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, chapterId3, assignment3);
        var assignmentId3 = 3L; // hard code

        // 3 submissions
        for (int i = 0; i < 3; i++) {
            // i is the assignment index
            // student submit assignment
            var submission = fileSubmissionAPI.getTemplateSubmission(assignmentId1 + i, student1Id);
            fileSubmissionAPI.submitFileSuccess(student1Token, assignmentId1 + i, submission, new MockMultipartFile
                    ("file", "test" + i + ".pdf", "application/pdf", new byte[0]));

            // teacher grades the assignment
            var submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId1 + i);
            fileSubmissionAPI.updateScoreSuccess(teacherToken, submissions.get(0).getFileSubmissionId(),
                    new FileSubmissionController.SubmissionScore((i + 1) * 30));
        }

        // create a study analysis
        var studyAnalysis = studyAnalysisAPI.getMyScoreSuccess(student1Token, course.getCourseId());
        var assignmentChapterSituationList = studyAnalysis.getContent();
        assert assignmentChapterSituationList.size() == 3;

        // check the result
        for (int i = 0; i < 3; i++) {
            var assignmentChapterSituation = assignmentChapterSituationList.get(i);
            assert assignmentChapterSituation.getChapterId() == assignmentId1 + i;
            assert assignmentChapterSituation.getChapterId() == chapterId + i;
            assert assignmentChapterSituation.getIsAllGraded();
            assert assignmentChapterSituation.getAssignments().size() == 1;
            var assignment = assignmentChapterSituation.getAssignments().get(0);
            assert assignment.getAssignment().getAssignmentId() == assignmentId1 + i;
        }

    }

    // 1 teacher , 1 course, 3 chapter, 3 assignments, 3 students, 9 submissions
    @Test
    public void testAll() throws Exception {
        // Create a teacher and login
        var teacherRes = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = teacherRes.first;
        var teacherId = teacherRes.second.getUserId();

        // Create a course
        var course = courseAPI.getTemplateCourse(teacherId);
        courseAPI.quickCreateCourse(course);
        var courseId = course.getCourseId();

        // Create 3 chapters, 1 for teaching, 1 for assignment, 1 for project
        var teachingChapterId = chapterAPI.addTestChapterAndGetId(courseId, Chapter.ChapterType.teaching);
        var assignmentChapterId = chapterAPI.addTestChapterAndGetId(courseId, Chapter.ChapterType.assignment);
        var projectChapterId = chapterAPI.addTestChapterAndGetId(courseId, Chapter.ChapterType.project);

        // Create assignments
        var assignment1 = assignmentAPI.getTemplateAssignment(assignmentChapterId);
        assignment1.setAssignmentName("Assignment1");
        assignment1.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, assignmentChapterId, assignment1);
        var assignmentId1 = 1L; // hard code

        var project = assignmentAPI.getTemplateAssignment(projectChapterId);
        project.setAssignmentName("Project1");
        project.setLatestSubmissionTime(OffsetDateTime.now().minusDays(1));
        assignmentAPI.createSuccess(teacherToken, projectChapterId, project);
        var projectId = 2L; // hard code

        // create resource
//        var templateResource = resourceAPI.getTemplateResource(teachingChapterId, "resource1",".mp4", Resource.ResourceType.video);
//        resourceAPI.uploadResourceSuccess(teacherToken, teachingChapterId, templateResource,
//                new MockMultipartFile("file", "test.mp4", "video/mp4", new byte[0]));
        var templateResourceId = resourceAPI.addTestResourceRecordAndGetId(teachingChapterId, Resource.ResourceType.video);

        // Create 3 students and login
        var student1Res = authAPI.quickAddUserAndLogin("student1", User.Role.student);
        var student1Token = student1Res.first;
        var student1Id = student1Res.second.getUserId();

        var student2Res = authAPI.quickAddUserAndLogin("student2", User.Role.student);
        var student2Token = student2Res.first;
        var student2Id = student2Res.second.getUserId();

        var student3Res = authAPI.quickAddUserAndLogin("student3", User.Role.student);
        var student3Token = student3Res.first;
        var student3Id = student3Res.second.getUserId();

        // Invite students to the course
        courseMemberAPI.inviteStudentsToCourseSuccess(teacherToken, courseId,
                List.of(student1Id, student2Id, student3Id));
        var studentList = courseMemberAPI.getStudentListSuccess(teacherToken, courseId, 1, -1);
        assertEquals(3, studentList.size(), "Should have 3 students enrolled");

        // 设置教学章节的完成情况
        // 学生1完成视频播放
        courseProgressAPI.completeResourceSuccess(student1Token,templateResourceId);
        // 学生2完成视频播放
        courseProgressAPI.completeResourceSuccess(student2Token,templateResourceId);


        // 学生1完成所有章节
        courseProgressAPI.completeChapterSuccess(student1Token, teachingChapterId);
        // 学生2完成部分章节
        courseProgressAPI.completeChapterSuccess(student2Token, teachingChapterId);
        // 学生3未完成任何章节

        // 设置作业章节的提交和评分情况
        // 学生1提交并获得高分
        submitAndGradeAssignment(student1Token, teacherToken, assignmentId1, 90, student1Id);
        // 学生2提交获得中等分数
        submitAndGradeAssignment(student2Token, teacherToken, assignmentId1, 70, student2Id);
        // 学生3提交但未评分
        submitAssignment(student3Token, assignmentId1, student3Id);

        // 设置项目章节的提交和评分情况
        // 学生1和学生2完成并获得评分
        submitAndGradeAssignment(student1Token, teacherToken, projectId, 95, student1Id);
        submitAndGradeAssignment(student2Token, teacherToken, projectId, 5, student2Id);
        // 学生3未提交

        // 测试教学章节的学习情况
        var teachingChapterResult = studyAnalysisAPI.teacherCheckChapterStudySituationSuccess(
                teacherToken, courseId, teachingChapterId);
        assertNotNull(teachingChapterResult, "Teaching chapter result should not be null");
        assertEquals(Chapter.ChapterType.teaching, teachingChapterResult.getChapterType());

        var teachingData = (StudyAnalysisController.TeachingChapter) teachingChapterResult.getData();
        assertEquals(1, teachingData.getUncompletedCount(), "Should have 1 uncompleted student");
        assertEquals(3, teachingData.getCompletedStatusList().size(), "Should have status for all 3 students");

        // 验证每个学生的完成状态
        var teachingStatusMap = teachingData.getCompletedStatusList().stream()
                .collect(Collectors.toMap(status -> status.getStudent().getUserId(),
                        TeachingChapter.CompletedStatus::getIsCompleted));
        assertTrue(teachingStatusMap.get(student1Id), "Student 1 should have completed");
        assertTrue(teachingStatusMap.get(student2Id), "Student 2 should have completed");
        assertFalse(teachingStatusMap.get(student3Id), "Student 3 should not have completed");

        // 测试作业章节的学习情况
        var assignmentChapterResult = studyAnalysisAPI.teacherCheckChapterStudySituationSuccess(
                teacherToken, courseId, assignmentChapterId);
        assertNotNull(assignmentChapterResult, "Assignment chapter result should not be null");
        assertEquals(Chapter.ChapterType.assignment, assignmentChapterResult.getChapterType());

        var assignmentData = (ScoreChapter) assignmentChapterResult.getData();
        assertEquals(80, assignmentData.getAverageScore(), "Average score should be 80");
        assertEquals(1, assignmentData.getUngradedCount(), "Should have 1 ungraded submission");
        assertEquals(0, assignmentData.getUncompletedCount(), "All students submitted");

        // 测试项目章节的学习情况
        var projectChapterResult = studyAnalysisAPI.teacherCheckChapterStudySituationSuccess(
                teacherToken, courseId, projectChapterId);
        assertNotNull(projectChapterResult, "Project chapter result should not be null");
        assertEquals(Chapter.ChapterType.project, projectChapterResult.getChapterType());

        var projectData = (ScoreChapter) projectChapterResult.getData();
        assertEquals(50, projectData.getAverageScore(), "Average score should be 50");
        assertEquals(0, projectData.getUngradedCount(), "Should have no ungraded submissions");
        assertEquals(1, projectData.getUncompletedCount(), "Should have 1 uncompleted student");

        // 测试课程整体学习情况
        var courseResult = studyAnalysisAPI.teacherCheckCourseStudySituationSuccess(
                teacherToken, courseId);
        assertNotNull(courseResult, "Course result should not be null");

        // 验证教学章节统计
        assertEquals(1, courseResult.getTeaching().getTotalChapterCount(),
                "Should have 1 teaching chapter");
        assertEquals(2, courseResult.getTeaching().getCompletedCount(),
                "2 students completed teaching chapter");
        assertEquals(3, courseResult.getTeaching().getMaxPossibleCompletedCount(),
                "Max possible completions is number of students");

        // 验证作业章节统计
        assertEquals(1, courseResult.getAssignment().getTotalChapterCount(),
                "Should have 1 assignment chapter");
        assertEquals(2, courseResult.getAssignment().getCompletedCount(),
                "2 students completed and graded");
        assertEquals(3, courseResult.getAssignment().getMaxPossibleCompletedCount(),
                "Max possible completions is number of students");

        // 验证项目章节统计
        assertEquals(1, courseResult.getProject().getTotalChapterCount(),
                "Should have 1 project chapter");
        assertEquals(2, courseResult.getProject().getCompletedCount(),
                "2 students completed and graded");
        assertEquals(3, courseResult.getProject().getMaxPossibleCompletedCount(),
                "Max possible completions is number of students");

        // 验证困难章节识别
        var difficultChapters = courseResult.getDifficultChapters();
        assertTrue(difficultChapters.stream()
                        .anyMatch(dc -> dc.getChapter().getChapterId().equals(projectChapterId)),
                "Project chapter should be identified as difficult");

        // 验证章节完成率变化
        var assignmentChangingRate = courseResult.getAssignment().getChangingRate();
        assertEquals(1, assignmentChangingRate.size(), "Should have 1 assignment chapter info");
        var assignmentInfo = assignmentChangingRate.get(0);
        assertEquals(80, assignmentInfo.getAverageScore(), "Assignment average score should be 80");
        assertEquals(90, assignmentInfo.getMaxScore(), "Assignment max score should be 90");
        assertEquals(2, assignmentInfo.getCompletedCount(), "2 students completed assignment");
        assertEquals(3, assignmentInfo.getMaxCompletedCount(), "Max possible completions is 3");

        var projectChangingRate = courseResult.getProject().getChangingRate();
        assertEquals(1, projectChangingRate.size(), "Should have 1 project chapter info");
        var projectInfo = projectChangingRate.get(0);
        assertEquals(50, projectInfo.getAverageScore(), "Project average score should be 50");
        assertEquals(95, projectInfo.getMaxScore(), "Project max score should be 95");
        assertEquals(2, projectInfo.getCompletedCount(), "2 students completed project");
        assertEquals(3, projectInfo.getMaxCompletedCount(), "Max possible completions is 3");
    }

    // 辅助方法：提交作业
    private void submitAssignment(String studentToken, Long assignmentId, Long studentId) throws Exception {
        var submission = fileSubmissionAPI.getTemplateSubmission(assignmentId, studentId);
        fileSubmissionAPI.submitFileSuccess(studentToken, assignmentId, submission,
                new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[0]));
    }

    // 辅助方法：提交作业并评分
    private void submitAndGradeAssignment(String studentToken, String teacherToken,
                                          Long assignmentId, int score, Long studentId) throws Exception {
        submitAssignment(studentToken, assignmentId, studentId);
        var submissions = fileSubmissionAPI.getAllSubmissionSuccess(teacherToken, assignmentId);
        fileSubmissionAPI.updateScoreSuccess(teacherToken,
                submissions.get(submissions.size() - 1).getFileSubmissionId(),
                new FileSubmissionController.SubmissionScore(score));
    }
}