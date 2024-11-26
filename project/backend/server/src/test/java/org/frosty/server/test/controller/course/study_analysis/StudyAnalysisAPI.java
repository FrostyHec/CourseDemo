package org.frosty.server.test.controller.course.study_analysis;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.test.testutils.MockUtil;
import org.frosty.server.controller.course.analysis.StudyAnalysisController;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@RequiredArgsConstructor
public class StudyAnalysisAPI {
    private final MockUtil mockUtil;
    private final String baseUrl = PathConstant.API + "/course";

    // Get all students' warnings
    public ResultActions getAllStudentsWarning(String teacherToken, Long courseId) throws Exception {
        String url = baseUrl + "/" + courseId + "/study/analysis/students-warning";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return mockUtil.performRequest(requestBuilder, teacherToken, null);
    }

    public StudyAnalysisController.WarningInfoList getAllStudentsWarningSuccess(String teacherToken, Long courseId) throws Exception {
        var resultActions = getAllStudentsWarning(teacherToken, courseId);
        return MockUtil.getSuccessResponse(resultActions, StudyAnalysisController.WarningInfoList.class);
    }

    // Get my warnings
    public ResultActions getMyWarning(String studentToken, Long courseId) throws Exception {
        String url = baseUrl + "/" + courseId + "/study/analysis/my-warning";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return mockUtil.performRequest(requestBuilder, studentToken, null);
    }

    public StudyAnalysisController.WarningInfoList getMyWarningSuccess(String studentToken, Long courseId) throws Exception {
        var resultActions = getMyWarning(studentToken, courseId);
        return MockUtil.getSuccessResponse(resultActions, StudyAnalysisController.WarningInfoList.class);
    }

    // Get all students' scores
    public ResultActions getAllStudentsScore(String teacherToken, Long courseId) throws Exception {
        String url = baseUrl + "/" + courseId + "/study/analysis/students-score";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return mockUtil.performRequest(requestBuilder, teacherToken, null);
    }

    public StudyAnalysisController.StudentsScoreTable getAllStudentsScoreSuccess(String teacherToken, Long courseId) throws Exception {
        var resultActions = getAllStudentsScore(teacherToken, courseId);
        return MockUtil.getSuccessResponse(resultActions, StudyAnalysisController.StudentsScoreTable.class);
    }

    // Get my scores
    public ResultActions getMyScore(String studentToken, Long courseId) throws Exception {
        String url = baseUrl + "/" + courseId + "/study/analysis/my-score";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return mockUtil.performRequest(requestBuilder, studentToken, null);
    }

    public StudyAnalysisController.AssignmentChapterSituationList getMyScoreSuccess(String studentToken, Long courseId) throws Exception {
        var resultActions = getMyScore(studentToken, courseId);
        return MockUtil.getSuccessResponse(resultActions, StudyAnalysisController.AssignmentChapterSituationList.class);
    }

    // Teacher check chapter study situation
    public ResultActions teacherCheckChapterStudySituation(String teacherToken, Long courseId, Long chapterId) throws Exception {
        String url = baseUrl + "/" + courseId + "/study/analysis/teacher/chapter/" + chapterId;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return mockUtil.performRequest(requestBuilder, teacherToken, null);
    }

    public StudyAnalysisController.ChapterStudySituation teacherCheckChapterStudySituationSuccess(String teacherToken, Long courseId, Long chapterId) throws Exception {
        var resultActions = teacherCheckChapterStudySituation(teacherToken, courseId, chapterId);
        return MockUtil.getSuccessResponse(resultActions, StudyAnalysisController.ChapterStudySituation.class);
    }

    // Teacher check course study situation
    public ResultActions teacherCheckCourseStudySituation(String teacherToken, Long courseId) throws Exception {
        String url = baseUrl + "/" + courseId + "/study/analysis/teacher/course";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return mockUtil.performRequest(requestBuilder, teacherToken, null);
    }

    public StudyAnalysisController.CourseStudySituation teacherCheckCourseStudySituationSuccess(String teacherToken, Long courseId) throws Exception {
        var resultActions = teacherCheckCourseStudySituation(teacherToken, courseId);
        return MockUtil.getSuccessResponse(resultActions, StudyAnalysisController.CourseStudySituation.class);
    }
}