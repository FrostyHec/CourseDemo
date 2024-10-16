package org.frosty.common_service.message_push.api;

import org.frosty.common_service.message_push.entity.EmailMessage;
import org.frosty.common_service.message_push.entity.SiteMessage;

public interface MessagePushService {
    void pushSite(SiteMessage siteMessage);

    void pushEmail(EmailMessage siteMessage);
}
