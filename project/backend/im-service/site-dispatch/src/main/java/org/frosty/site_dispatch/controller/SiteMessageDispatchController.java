package org.frosty.site_dispatch.controller;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.site_dispatch.entity.MessageType;
import org.frosty.site_dispatch.entity.SingleMessageDTO;
import org.frosty.site_dispatch.service.MessageDispatchService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(PathConstant.INTERNAL_API + "/msg/site")
@RequiredArgsConstructor
public class SiteMessageDispatchController {
    private final MessageDispatchService service;

    @PostMapping
    public Response push(@RequestBody SingleMessageDTO msg) {
        Ex.check(!(msg.getType() != MessageType.NEW && msg.getMessageId() == null),
                Response.getBadRequest("msgid-missing"));
        long mid = service.push(msg);
        return Response.getSuccess(Map.of("mid", mid));
    }

    @PatchMapping("/ack/{mid}")
    public Response ack(@PathVariable long mid) {
        service.ack(mid);
        return Response.getSuccess();
    }
}
