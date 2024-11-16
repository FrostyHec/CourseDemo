package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstant.API+"resource/{id}/watch")
@RequiredArgsConstructor
public class CheatCheckController {
    //TODO
    @PutMapping("/required-time")
    public void setMinRequiredTime(@PathVariable Long id,
                                           @GetToken TokenInfo tokenInfo,
                                       @RequestBody SetMinRequiredTimeEntity setMinRequiredTimeEntity) {
        FrameworkUtils.notImplemented();
    }
    @GetMapping("/required-time")
    public SetMinRequiredTimeEntity enrollStudentsToCourse(@PathVariable Long id,
                                       @GetToken TokenInfo tokenInfo) {
        FrameworkUtils.notImplemented();
        return null;
    }

    @GetMapping("/last")
    public LastWatchedEntity getLastWatched(@PathVariable Long id,
                                   @GetToken TokenInfo tokenInfo) {
        FrameworkUtils.notImplemented();
        return null;
    }

    @PutMapping("/start")
    public void startWatchAlive(@PathVariable Long id,
                                            @GetToken TokenInfo tokenInfo) {
        FrameworkUtils.notImplemented();
    }

    @GetMapping("/alive")
    public void keepWatchAlive(@PathVariable Long id,
                                            @GetToken TokenInfo tokenInfo) {
        FrameworkUtils.notImplemented();
    }

    @GetMapping("/stop")
    public void stopWatchAlive(@PathVariable Long id,
                                            @GetToken TokenInfo tokenInfo,@RequestBody WatchedInfoEntity watchedInfoEntity) {
        FrameworkUtils.notImplemented();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetMinRequiredTimeEntity {
        private Long requiredSeconds;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LastWatchedEntity {
        private Long remainRequiredSeconds;
        private Long lastWatchedSeconds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WatchedInfoEntity {
        private Long watchedSeconds;
        private Long watchedUntil;
    }
}
