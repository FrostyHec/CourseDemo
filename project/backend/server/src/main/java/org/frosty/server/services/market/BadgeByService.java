package org.frosty.server.services.market;

import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.entity.bo.market.BadgeInfo;

public interface BadgeByService {
    BadgeByController.BadgeList getMyBadge(Long userID);

    void buyBadge(Long userId, BadgeInfo badgeInfo);

    BadgeByController.BadgeList getMyCanByBadge(Long userID);
}
