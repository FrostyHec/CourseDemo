package org.frosty.email_push.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.common.constant.PathConstant;
import org.frosty.common_service.im.entity.Email;
import org.frosty.email_push.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(PathConstant.INTERNAL_API + "/msg/email")
@RequiredArgsConstructor
public class EmailPushController {
    private final EmailService emailService;

    @PostMapping
    public void sendEmail(@RequestBody Email email) {
        emailService.send(email);
    }
}
