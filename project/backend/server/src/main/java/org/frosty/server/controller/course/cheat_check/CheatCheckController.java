package org.frosty.server.controller.course.cheat_check;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.AuthEx;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.cheat_check.VideoRequiredSeconds;
import org.frosty.server.entity.bo.cheat_check.VideoWatchRecord;
import org.frosty.server.services.course.cheat_check.CheatCheckService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstant.API + "/resource/{id}/watch")
@RequiredArgsConstructor
public class CheatCheckController {
    private final CheatCheckService cheatCheckService;

    //TODO auth check
    @PutMapping("/required-time")
    public void setMinRequiredTime(@PathVariable Long id,
                                   @GetToken TokenInfo tokenInfo,
                                   @RequestBody VideoRequiredSeconds videoRequiredSeconds) {
        videoRequiredSeconds.setVideoId(id);
        cheatCheckService.setMinRequiredTime(videoRequiredSeconds);
    }

    @GetMapping("/required-time")
    public VideoRequiredSeconds enrollStudentsToCourse(@PathVariable Long id,
                                                       @GetToken TokenInfo tokenInfo) {
        return cheatCheckService.getMinRequiredTime(id);
    }

    @GetMapping("/last")
    public VideoWatchRecord getLastWatched(@PathVariable Long id,
                                           @GetToken TokenInfo tokenInfo) {
        var auth = AuthEx.checkPass(tokenInfo);
        return cheatCheckService.getWatchedRecord(id, auth);
    }

    @PutMapping("/start")
    public void startWatchAlive(@PathVariable Long id,
                                @GetPassedToken AuthInfo auth) {
        cheatCheckService.startWatchAlive(id, auth.getUserID());
    }

    @GetMapping("/alive")
    public void keepWatchAlive(@PathVariable Long id,
                               @GetToken TokenInfo tokenInfo) {
        var auth = AuthEx.checkPass(tokenInfo);
        cheatCheckService.keepWatchAlive(id, auth);
    }

    @GetMapping("/stop")
    public void stopWatchAlive(@PathVariable Long id,
                               @GetToken TokenInfo tokenInfo, String watched_seconds, String watched_until) {
        int watchedSeconds = (int) Double.parseDouble(watched_seconds),// watched seconds是start-end的时间，until是end的时间
                watchedUntil = (int) Double.parseDouble(watched_until);
        var watchedInfoEntity = new WatchedInfoEntity(watchedSeconds, watchedUntil);
        var auth = AuthEx.checkPass(tokenInfo);
        cheatCheckService.stopWatchAlive(id, auth, watchedInfoEntity);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WatchedInfoEntity {
        private Integer watchedSeconds;
        private Integer watchedUntil;
    }
}
