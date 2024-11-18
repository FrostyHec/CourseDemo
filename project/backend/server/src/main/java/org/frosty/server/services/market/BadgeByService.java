package org.frosty.server.services.market;

import org.frosty.server.controller.market.BadgeByController;

public interface BadgeByService {
    BadgeByController.BadgeList getMyBadge();

    void buyBadge(BadgeByController.BadgeInfo badgeInfo);

    BadgeByController.BadgeList getMyCanByBadge(BadgeByController.BadgeInfo badgeInfo);
}
