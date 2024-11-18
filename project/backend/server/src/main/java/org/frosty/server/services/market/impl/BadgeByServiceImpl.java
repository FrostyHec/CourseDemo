package org.frosty.server.services.market.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.mapper.market.BadgeByMapper;
import org.frosty.server.services.market.BadgeByService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BadgeByServiceImpl implements BadgeByService {
    private final BadgeByMapper badgeByMapper;
    @Override
    public BadgeByController.BadgeList getMyBadge() {
        return badgeByMapper.selectMyBadge();
    }

    @Override
    public void buyBadge(BadgeByController.BadgeInfo badgeInfo) {
        badgeByMapper.insertBadge(badgeInfo);
    }

    @Override
    public BadgeByController.BadgeList getMyCanByBadge(BadgeByController.BadgeInfo badgeInfo) {
        return badgeByMapper.selectMyCanByBadge();
    }
}
