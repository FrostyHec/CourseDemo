package org.frosty.server.controller.course.livestream;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.config.AuthConstant;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import org.frosty.common_service.storage.api.SharedBiMapService;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstant.INTERNAL_API + "/auth/live-stream/course")
@RequiredArgsConstructor
public class InternalLiveStreamAuthController {
    // TODO 比较懒直接在controller里写了
    private final SharedBiMapService sharedBiMapService;

    @PostConstruct
    public void init(){
        sharedBiMapService.init("course.livestream");
    }

    @PostMapping("/push")
    public ResponseEntity<String> pushAuth(@ModelAttribute PushAuthEntity pushAuthEntity) {
        String name = pushAuthEntity.getName();
//    @PostMapping("/push")
//    public ResponseEntity<String> pushAuth(@RequestHeader("X-Original-URI") String uri) {
//        String name = getPushName(uri);
        boolean isValid = sharedBiMapService.valueExist(name);
        if (!isValid&&"course".equals(pushAuthEntity.getApp())) {
            return getUnauthorized();
        }
        return ResponseEntity.ok("ok");
    }

    public ResponseEntity<String> getUnauthorized() {
        return ResponseEntity.status(AuthConstant.internalUnauthorizedCode).body("unauthorized");
    }

    @GetMapping("/pull")
    public ResponseEntity<String> pullAuth(@GetToken TokenInfo tokenInfo, @RequestHeader("X-Original-URI") String uri) {
        String name = getPullName(uri);
        String cid = sharedBiMapService.getKeyByValue(name);
        if (cid == null || tokenInfo.getAuthStatus() != AuthStatus.PASS) {
            return getUnauthorized();
        }
        AuthInfo info = tokenInfo.getAuthInfo();
        // TODO check是否有权限
        return ResponseEntity.ok("ok");
    }
    private String getPushName(String uri){
        try {
            String[] arr = uri.split("course/");
            assert arr.length == 2;
            String name = arr[1];
            assert name != null;
            return name;
        } catch (Exception e) {
            throw new InternalException("bad uri:" + uri + "e:" + e);
        }
    }
    private String getPullName(String uri) {
        try {
            String[] arr = uri.split("stream=");
            assert arr.length == 2;
            String name = arr[1].split("&")[0];
            assert name != null;
            return name;
        } catch (Exception e) {
            throw new InternalException("bad uri:" + uri + "e:" + e);
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PushAuthEntity{
        private String app;
        private String name;
    }
}
