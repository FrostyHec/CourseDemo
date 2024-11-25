package org.frosty.server.services.market;

import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.entity.bo.market.BadgeInfo;

import java.util.List;

public interface BadgeByService {
    List<BadgeInfo> getMyBadge(Long userID);

    void buyBadge(Long userId, BadgeInfo badgeInfo);

    List<BadgeInfo> getMyCanByBadge(Long userID);
}
