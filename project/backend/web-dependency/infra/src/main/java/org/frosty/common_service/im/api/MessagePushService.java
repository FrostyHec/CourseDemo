package org.frosty.common_service.im.api;

import org.frosty.common_service.im.entity.Email;
import org.frosty.common_service.im.entity.SiteMessage;

public interface MessagePushService {
    SiteMessage pushSite(SiteMessage siteMessage);

    void ackSite(Long mid);

    void pushEmail(Email email);
}
