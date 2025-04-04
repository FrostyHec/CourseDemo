package org.frosty.server.test.controller.smoke_test.course.progress;

import org.frosty.server.entity.bo.Resource;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.bo.cheat_check.VideoRequiredSeconds;
import org.frosty.server.entity.bo.cheat_check.VideoWatchRecord;
import org.frosty.server.mapper.course.cheat_check.VideoWatchedRecordMapper;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.cheat_check.CheatCheckAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.progress.CourseProgressAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.server.test.controller.market.MarketHistoryAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.frosty.test_common.utils.RespChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static org.frosty.server.entity.bo.market.ConsumeRecord.ConsumeActionType.complete_course;
import static org.frosty.server.entity.bo.market.ConsumeRecord.ConsumeActionType.daily_comment;

@IdempotentControllerTest
public class CourseProgressSmokeTest {
    // 测试1resource1chapter1course的情况下直接调用complete resource/chapter/course会失败（高级测试）
    // 测试能够正常的complete resource，chapter和course
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private CheatCheckAPI cheatCheckAPI;
    @Autowired
    private CourseProgressAPI courseProgressAPI;
    @Autowired
    private ChapterAPI chapterAPI;
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private ResourceAPI resourceAPI;
    @Autowired
    private MarketHistoryAPI marketHistoryAPI;

    @Test
    public void testCompleteResource() throws Exception {
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

        // 创建课程
        var courseId = courseAPI.addTestCourseAndGetId(teacher.getUserId());
        // 创建章节
        var chapterId = chapterAPI.addTestChapterAndGetId(courseId);
        // 创建资源
        var videoId = resourceAPI.addTestResourceRecordAndGetId(chapterId, Resource.ResourceType.video);
        var videoId2 = resourceAPI.addTestResourceRecordAndGetId(chapterId, Resource.ResourceType.video);
        var otherResource = resourceAPI.addTestResourceRecordAndGetId(chapterId, Resource.ResourceType.courseware);
        cheatCheckAPI.setMinRequiredTimeSuccess(teacherToken, videoId, new VideoRequiredSeconds(null, 2));

        // ----------- test init -----------
        // 测试1resource1chapter1course的情况下直接调用complete resource/chapter/course会失败
        courseProgressAPI.completeResource(studentToken, videoId)
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("video-"+videoId+"-not-complete"));

        courseProgressAPI.completeChapter(studentToken, courseId)
                .andExpect(RespChecker.badRequest())
                ;

        courseProgressAPI.completeCourse(studentToken, courseId)
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("chapter-"+chapterId+"-not-complete"));

        //测试能够正常的complete resource，chapter和course
        cheatCheckAPI.addComplete(videoId, student.getUserId());
        courseProgressAPI.completeResourceSuccess(studentToken, videoId);
        courseProgressAPI.completeChapter(studentToken, courseId)
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("resource-"+videoId2+"-not-complete"));
        courseProgressAPI.completeResourceSuccess(studentToken, videoId2);
        courseProgressAPI.completeChapterSuccess(studentToken, courseId);
        courseProgressAPI.completeCourseSuccess(studentToken, courseId);


        var History = marketHistoryAPI.getMyHistorySuccess(studentToken);
        System.out.println("-----------------------");
        System.out.println(History);
        System.out.println("-----------------------");
        assert History.size() == 1;
        assert History.get(0).getActionType().equals(complete_course);



        var progress = courseProgressAPI.checkCourseProgressSuccess(studentToken, courseId);
        assert progress.getCourseId().equals(courseId);
        assert progress.getIsCompleted();
        var chapter = CommonCheck.checkSingleAndGet(progress.getChapterProgress());
        assert chapter.getIsCompleted();
        for (var r : chapter.getVideoResources()) {
            assert Objects.equals(r.getResourceId(), videoId)
                    || Objects.equals(r.getResourceId(), videoId2);
            assert r.getIsCompleted();
        }

    }
}
