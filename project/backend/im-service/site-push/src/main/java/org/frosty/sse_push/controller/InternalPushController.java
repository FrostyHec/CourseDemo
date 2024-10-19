package org.frosty.sse_push.controller;

import java.util.Map;

import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.sse_push.entity.SingleMessageDTO;
import org.frosty.sse_push.service.SSEService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(PathConstant.INTERNAL_API + "/msg/site")
@RequiredArgsConstructor
public class InternalPushController {
    private final SSEService service;

    @PostMapping("/push")
    public Response push(@RequestBody SingleMessageDTO dto) {
        log.info("pushing message:"+dto);
        long id =  service.push(dto);
        log.warn("message pushed with no error:"+dto);
        return Response.getSuccess(Map.of("mid",id));
    }
}
