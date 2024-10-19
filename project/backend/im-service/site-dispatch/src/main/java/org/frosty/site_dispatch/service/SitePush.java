package org.frosty.site_dispatch.service;

import org.frosty.site_dispatch.entity.SingleMessageDTO;

public interface SitePush {
    long pushMessage(String handlerIp, SingleMessageDTO msg);
}
