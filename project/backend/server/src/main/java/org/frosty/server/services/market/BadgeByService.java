package org.frosty.server.services.market;

import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.entity.bo.market.BadgeInfo;

public interface BadgeByService {
    BadgeByController.BadgeList getMyBadge();

    void buyBadge(BadgeInfo badgeInfo);

    BadgeByController.BadgeList getMyCanByBadge();
}
