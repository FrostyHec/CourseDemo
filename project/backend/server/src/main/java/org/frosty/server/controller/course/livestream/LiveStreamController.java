package org.frosty.server.controller.course.livestream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.AuthEx;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.services.course.livestream.LiveStreamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class LiveStreamController {
    private final LiveStreamService service;
    @GetMapping("/course/{courseId}/live-stream/push")
    public LivestreamName getPushName(@GetToken TokenInfo tokenInfo, @PathVariable Long courseId) {
        AuthInfo info = AuthEx.checkPass(tokenInfo);
        // TODO  check is teacher of this course
        return new LivestreamName(service.getPushName(courseId));
    }
    @GetMapping("/course/{courseId}/live-stream/pull")
    public LivestreamName getPullName(@GetToken TokenInfo tokenInfo, @PathVariable Long courseId) {
        AuthInfo info = AuthEx.checkPass(tokenInfo);
        // TODO  check is student or teacher of this course
        return new LivestreamName(service.getPullName(courseId));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LivestreamName {
        private String name;
    }
}
