package org.frosty.email_push.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.common.exception.InternalException;
import org.frosty.common_service.im.entity.Email;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void send(Email email) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom(email.getFromEmail(), email.getFromName());
            helper.setTo(email.getToEmail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText());
            log.info("sending email:"+email);
            javaMailSender.send(msg);
        } catch (Exception e) {
            throw new InternalException("cannot send email email:" + email + "\n,e:" + e);
        }
    }
}
