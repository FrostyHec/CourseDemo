package org.frosty.sse_push.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.sse_push.service.SSEService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@RequestMapping(PathConstant.API + "/msg/site")
@RequiredArgsConstructor
public class PublicRegisterController {
    private final SSEService service;

    @GetMapping("/user/{uid}")
    public SseEmitter register(@GetToken TokenInfo tokenInfo, @PathVariable long uid) {
        Ex.check((tokenInfo.getAuthStatus() == AuthStatus.PASS)
                        && (tokenInfo.getAuthInfo().getUserID() == uid)
                , Response.getUnauthorized("unauthorized"));
        log.info("connection creating on uid:" + uid);
        return service.register(uid);
    }
// TODO LET TEST SUPPORT AUTH
//    @GetMapping("/user/{uid}")
//    public SseEmitter register(@PathVariable long uid) {
//        log.info("connection creating on uid:" + uid);
//        return service.register(uid);
//    }
}
