package org.frosty.server.test.controller.smoke_test.course.cheat_check;

import org.frosty.server.controller.course.cheat_check.CheatCheckController;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.bo.cheat_check.VideoRequiredSeconds;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.cheat_check.CheatCheckAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.frosty.test_common.utils.RespChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.TimeUnit;

@IdempotentControllerTest
public class CheatCheckSmokeTest {
    // 0. 通过反射设置RECORD_ALIVE_SECONDS和HEART_BEAT_INTERVAL_SECONDS
    // 1. 教师设置rq time，获得rq-time
    // 2. 学生查看自己的剩余时间
    // 3. 学生开始播放视频并且在一分钟内截止，返回一个合法的 / 非法的时间；最后检查
    // 4. 学生播放视频并且超过一分钟时间截止，返回一个合法的 / 非法的时间；最后检查
    // 5. 学生播放视频后直接播放下一个视频，查看新旧视频的时间变化
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private CheatCheckAPI cheatCheckAPI;

    @Test
    public void testSetMinRequiredTime() throws Exception {
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

        long videoId = 1L;
        int requiredSeconds = 3;
        int hbSeconds = 2;
        int wtMilliSeconds = 2100;
        int wtMilliSeconds2 = 1000;
        cheatCheckAPI.resetTimeConfig(5*60, hbSeconds);
        cheatCheckAPI.setMinRequiredTimeSuccess(teacherToken,videoId, new VideoRequiredSeconds(null, requiredSeconds));
        var rqTime = cheatCheckAPI.getMinRequiredTimeSuccess(studentToken, videoId);
        assert rqTime.getVideoId() == videoId;
        assert rqTime.getRequiredSeconds() == requiredSeconds;

        // 2. 学生查看自己的剩余时间
        var rqTime2 = cheatCheckAPI.getLastWatchedSuccess(studentToken, videoId);
        assert rqTime2.getRemainRequiredSeconds() == requiredSeconds;

        // 3. 学生开始播放视频并且跳动一个beat，返回一个合法的 / 非法的时间；最后检查
        cheatCheckAPI.startWatchAliveSuccess(studentToken, videoId);
        //太快的心跳
        cheatCheckAPI.keepWatchAlive(studentToken, videoId)
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("heart-beat-abnormal"));
        TimeUnit.MILLISECONDS.sleep(wtMilliSeconds);
        //正常心跳
        cheatCheckAPI.keepWatchAliveSuccess(studentToken, videoId);
        //结束
        cheatCheckAPI.stopWatchAlive(studentToken,
                videoId,
                new CheatCheckController.WatchedInfoEntity(requiredSeconds,requiredSeconds)
        ).andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("stop-abnormal"));

        cheatCheckAPI.stopWatchAliveSuccess(studentToken,
                videoId,
                new CheatCheckController.WatchedInfoEntity(wtMilliSeconds/1000,wtMilliSeconds/1000)
        );

        var rqRime2 = cheatCheckAPI.getLastWatchedSuccess(studentToken, videoId);
        assert rqRime2.getRemainRequiredSeconds() == requiredSeconds-wtMilliSeconds/1000;
        assert rqRime2.getLastWatchedSeconds() == wtMilliSeconds/1000;

        // 4. 学生播放视频并且不跳动beat便终止，返回一个合法的 / 非法的时间；最后检查
        cheatCheckAPI.startWatchAliveSuccess(studentToken, videoId);
        //太快的心跳
        cheatCheckAPI.keepWatchAlive(studentToken, videoId)
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("heart-beat-abnormal"));
        TimeUnit.MILLISECONDS.sleep(wtMilliSeconds2);
        //结束
        cheatCheckAPI.stopWatchAliveSuccess(studentToken,
                videoId,
                new CheatCheckController.WatchedInfoEntity(wtMilliSeconds2/1000,(wtMilliSeconds+wtMilliSeconds2)/1000)
        );

        var rqRime3 = cheatCheckAPI.getLastWatchedSuccess(studentToken, videoId);
        assert rqRime3.getRemainRequiredSeconds() == 0;
        assert rqRime3.getLastWatchedSeconds() == (wtMilliSeconds+wtMilliSeconds2)/1000;
    }

    @Test
    public void testForgotCloseAutoCloseAndSetRqTime() throws Exception {
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

        long videoId = 1L;
        int requiredSeconds = 3;
        int hbSeconds = 2;
        int timeOutSeconds = 3;
        int wtMilliSeconds = 2100;
        cheatCheckAPI.resetTimeConfig(timeOutSeconds, hbSeconds);
        cheatCheckAPI.setMinRequiredTimeSuccess(teacherToken,videoId, new VideoRequiredSeconds(null, requiredSeconds));
        var rqTime = cheatCheckAPI.getMinRequiredTimeSuccess(studentToken, videoId);
        assert rqTime.getVideoId() == videoId;
        assert rqTime.getRequiredSeconds() == requiredSeconds;

        // 2. 学生查看自己的剩余时间
        var rqTime2 = cheatCheckAPI.getLastWatchedSuccess(studentToken, videoId);
        assert rqTime2.getRemainRequiredSeconds() == requiredSeconds;
        // 3. 学生开始后就timeout
        cheatCheckAPI.startWatchAliveSuccess(studentToken, videoId);
        TimeUnit.SECONDS.sleep(timeOutSeconds);
        TimeUnit.MILLISECONDS.sleep(500);

        rqTime2 = cheatCheckAPI.getLastWatchedSuccess(studentToken, videoId);
        assert rqTime2.getRemainRequiredSeconds() == requiredSeconds;
        assert rqTime2.getLastWatchedSeconds() == 0;

        // 4. 学生开始播放视频并且跳动一个beat，然后timeout
        cheatCheckAPI.startWatchAliveSuccess(studentToken, videoId);
        //太快的心跳
        TimeUnit.MILLISECONDS.sleep(wtMilliSeconds);
        //正常心跳
        cheatCheckAPI.keepWatchAliveSuccess(studentToken, videoId);
        //不结束
        TimeUnit.SECONDS.sleep(timeOutSeconds);
        TimeUnit.MILLISECONDS.sleep(500);

        rqTime2 = cheatCheckAPI.getLastWatchedSuccess(studentToken, videoId);
        assert rqTime2.getRemainRequiredSeconds() == requiredSeconds-hbSeconds;
        assert rqTime2.getLastWatchedSeconds() == 0;

    }

}
