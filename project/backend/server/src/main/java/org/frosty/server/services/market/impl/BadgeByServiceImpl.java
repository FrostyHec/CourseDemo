package org.frosty.server.services.market.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.entity.bo.market.BadgeInfo;
import org.frosty.server.mapper.market.BadgeByMapper;
import org.frosty.server.services.market.BadgeByService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BadgeByServiceImpl implements BadgeByService {
    private final BadgeByMapper badgeByMapper;

    @Override
    public BadgeByController.BadgeList getMyBadge(long userID) {
        return badgeByMapper.selectMyBadge(userID);
    }

    @Override
    public void buyBadge(BadgeInfo badgeInfo) {
        // TODO 不知道 BaseMapper 能不能成功解析出这个
        badgeByMapper.insert(badgeInfo);
    }

    @Override
    public BadgeByController.BadgeList getMyCanByBadge(long userID) {
        return badgeByMapper.selectMyCanByBadge(userID);
    }
}
