package org.frosty.site_dispatch.service;

import org.frosty.sse.entity.SiteMessage;

public interface SitePush {
    void pushMessage(String handlerIp, SiteMessage msg);
}
