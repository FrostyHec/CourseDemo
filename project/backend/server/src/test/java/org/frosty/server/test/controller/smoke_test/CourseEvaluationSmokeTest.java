package org.frosty.server.test.controller.smoke_test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.frosty.server.controller.course.CourseEvaluationController;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.course_evaluaion.CourseEvaluationAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@IdempotentControllerTest
public class CourseEvaluationSmokeTest {

    @Autowired
    private CourseAPI courseAPI;

    @Autowired
    private CourseEvaluationAPI courseEvaluationAPI;

    @Autowired
    private AuthAPI authAPI;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCourseEvaluationCRUD() throws Exception {
        // Teacher login
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        // Create a course
        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Student login
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        var courseId = courseAPI.
                searchPublicCourseSuccess(studentToken, 1, -1, "testCourse")
                .get(0).getCourseId();
        course.setCourseId(courseId);

        // 非空或空的json字符串都可以
        String jsonString = "{\"name\":\"John\", \"age\":30}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Create course evaluation
        CourseEvaluation courseEvaluation = new CourseEvaluation()
                .setCourseId(course.getCourseId())
                .setStudentId(student.getUserId())
                .setComment("Great course!")
                .setEvaluationFormAnswer(jsonNode)
                .setScore(5);
        courseEvaluationAPI.createEvaluationSuccess(studentToken, course.getCourseId(), courseEvaluation);

        // Get course evaluation
        CourseEvaluation createdEvaluation = courseEvaluationAPI.getEvaluationSuccess(studentToken, course.getCourseId());
        assertNotNull(createdEvaluation);
        assertEquals("Great course!", createdEvaluation.getComment());

        // Update course evaluation
        createdEvaluation.setComment("Updated comment");
        courseEvaluationAPI.updateEvaluationSuccess(studentToken, course.getCourseId(), createdEvaluation);

        // Get updated course evaluation
        CourseEvaluation updatedEvaluation = courseEvaluationAPI.getEvaluationSuccess(studentToken, course.getCourseId());
        assertEquals("Updated comment", updatedEvaluation.getComment());

        // Get all course evaluations
        List<CourseEvaluation> evaluations = courseEvaluationAPI.getAllEvaluationsSuccess(studentToken, course.getCourseId(), 1, 1);
        assertFalse(evaluations.isEmpty());

        // Delete course evaluation
        courseEvaluationAPI.deleteEvaluationSuccess(studentToken, course.getCourseId());

        // Verify deletion
        CourseEvaluation deletedEvaluation = courseEvaluationAPI.getEvaluationSuccess(studentToken, course.getCourseId());
        assertNull(deletedEvaluation);
    }

    @Test
    public void testGetAllEvaluationsSuccessWithComplexCases() throws Exception {
        // Teacher login
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        // Create a course
        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Student login
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        var courseId = courseAPI.
                searchPublicCourseSuccess(studentToken, 1, -1, "testCourse")
                .get(0).getCourseId();
        course.setCourseId(courseId);

        // 非空或空的json字符串都可以
        String jsonString = "{\"name\":\"John\", \"age\":30}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Create multiple course evaluations with different student IDs
        for (int i = 1; i <= 5; i++) {
            // Create a new student for each evaluation to avoid primary key conflict
            pair = authAPI.quickAddUserAndLogin("student" + i, User.Role.student);
            var newStudentToken = pair.first;
            var newStudent = pair.second;

            CourseEvaluation courseEvaluation = new CourseEvaluation()
                    .setCourseId(course.getCourseId())
                    .setStudentId(newStudent.getUserId())
                    .setComment("Comment " + i)
                    .setEvaluationFormAnswer(jsonNode)
                    .setScore(i);
            courseEvaluationAPI.createEvaluationSuccess(newStudentToken, course.getCourseId(), courseEvaluation);
        }

        // Get all course evaluations with pagination
        List<CourseEvaluation> evaluationsPage1 = courseEvaluationAPI.getAllEvaluationsSuccess(studentToken, course.getCourseId(), 3, 1);
        assertEquals(3, evaluationsPage1.size());
        assertEquals("Comment 1", evaluationsPage1.get(0).getComment());
        assertEquals("Comment 2", evaluationsPage1.get(1).getComment());
        assertEquals("Comment 3", evaluationsPage1.get(2).getComment());

        List<CourseEvaluation> evaluationsPage2 = courseEvaluationAPI.getAllEvaluationsSuccess(studentToken, course.getCourseId(), 3, 2);
        assertEquals(2, evaluationsPage2.size());
        assertEquals("Comment 4", evaluationsPage2.get(0).getComment());
        assertEquals("Comment 5", evaluationsPage2.get(1).getComment());
    }

    @Test
    public void testGetEvaluationMetaData() throws Exception {
        // create a course
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);


        // create 2 students
        var studentPair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = studentPair.first;
        var student = studentPair.second;

        var studentPair2 = authAPI.quickAddUserAndLogin("student2", User.Role.student);
        var studentToken2 = studentPair2.first;
        var student2 = studentPair2.second;

        var courseId = courseAPI.
                searchPublicCourseSuccess(studentToken, 1, -1, "testCourse")
                .get(0).getCourseId();
        course.setCourseId(courseId);

        String jsonString = "{\"name\":\"John\", \"age\":30}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // insert evaluation for 2 times
        CourseEvaluation courseEvaluation = new CourseEvaluation()
                .setCourseId(course.getCourseId())
                .setStudentId(student.getUserId())
                .setComment("Great course!")
                .setEvaluationFormAnswer(jsonNode)
                .setScore(5);
        CourseEvaluation courseEvaluation1 = new CourseEvaluation()
                .setCourseId(course.getCourseId())
                .setStudentId(student2.getUserId())
                .setComment("Bad course!")
                .setEvaluationFormAnswer(jsonNode)
                .setScore(1);
        courseEvaluationAPI.createEvaluationSuccess(studentToken, course.getCourseId(), courseEvaluation);
        courseEvaluationAPI.createEvaluationSuccess(studentToken2, course.getCourseId(), courseEvaluation1);

        CourseEvaluationController.CourseEvaluationMetadata metadata = courseEvaluationAPI.getEvaluationMetadataSuccess(studentToken, course.getCourseId());
        assertEquals(3, metadata.getAverageScore());
    }
}