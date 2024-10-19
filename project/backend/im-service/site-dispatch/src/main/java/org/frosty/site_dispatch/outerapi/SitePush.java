package org.frosty.site_dispatch.outerapi;

import org.frosty.site_dispatch.entity.SingleMessageDTO;

public interface SitePush {
    long pushMessage(String handlerIp, SingleMessageDTO msg);
}
