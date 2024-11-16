package org.frosty.common_service.im.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.common_service.im.entity.Email;
import org.frosty.common_service.im.entity.SiteMessage;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MockMessagePushServiceImpl implements MessagePushService {
    public AtomicLong messageId = new AtomicLong(0);

    @Override
    public SiteMessage pushSite(SiteMessage siteMessage) {
        var next = messageId.incrementAndGet();
        siteMessage.setMessageId(next);
        return siteMessage;
    }

    @Override
    public void ackSite(Long mid) {
        log.info("ack site message: {}", mid);
    }

    @Override
    public void pushEmail(Email email) {
        log.info("push email: {}", email);
    }
}
