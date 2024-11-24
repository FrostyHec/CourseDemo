package org.frosty.server.services.market.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.entity.bo.market.BadgeInfo;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.entity.bo.market.action_type.BuyBadgeActionParam;
import org.frosty.server.mapper.market.BadgeByMapper;
import org.frosty.server.services.market.BadgeByService;
import org.frosty.server.services.market.MarketHistoryService;
import org.frosty.server.services.market.MarketService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class BadgeByServiceImpl implements BadgeByService {
    private final BadgeByMapper badgeByMapper;
    private final MarketService marketService;

    private final MarketHistoryService marketHistoryService;

    @Override
    public BadgeByController.BadgeList getMyBadge(Long userID) {
        return badgeByMapper.selectMyBadge(userID);
    }

    @Override
    public void buyBadge(Long userId, BadgeInfo badgeInfo) {
        int marketScore = marketService.getMyMarketScore(userId).getMarketScore();
        Ex.check(badgeInfo.getMarketScore() > marketScore, Response.getBadRequest("积分不足"));
        badgeByMapper.insertBadge(badgeInfo);


        // 计算购买后剩余的积分
        int remainingScore = marketScore - badgeInfo.getMarketScore();

        // 构建操作参数
        BuyBadgeActionParam actionParam = new BuyBadgeActionParam();
        actionParam.setBadgeId(badgeInfo.getBadgeId());
        actionParam.setBadgeName(badgeInfo.getBadgeName());

        // 构建消费记录
        ConsumeRecord consumeRecord = new ConsumeRecord()
                .setUserId(userId)
                .setActionType(ConsumeRecord.ConsumeActionType.buy_badge)
                .setActionParam(actionParam)
                .setChangedScore(-badgeInfo.getMarketScore())  // 消耗积分
                .setRemainScore(remainingScore);

        // 插入消费记录到数据库
        marketHistoryService.insertConsumeRecord(consumeRecord);

        // 更新用户市场积分
        marketService.addUserMarketScore(userId, -badgeInfo.getMarketScore());  // 减去消费的积分

    }

    @Override
    public BadgeByController.BadgeList getMyCanByBadge(Long userID) {
        return badgeByMapper.selectMyCanByBadge(userID);
    }
}
