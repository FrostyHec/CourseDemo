package org.frosty.server.services.market.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.server.controller.market.EarnCreditEventHandler;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;
import org.frosty.server.mapper.market.EarnCreditEventMapper;
import org.frosty.server.services.market.EarnCreditEventService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EarnCreditEventServiceImp implements EarnCreditEventService {
    private final EarnCreditEventMapper earnCreditEventMapper;
    private final MessagePushService messagePushService;

    @Override
    public void handleCourseComplete(CompleteCourseEvent event) {
        // TODO: 根据逻辑完成
        Long userId = null;
        earnCreditEventMapper.insertCourseCompleteHistory();
        earnCreditEventMapper.addUserMarketScore(userId, EarnCreditEventHandler.ScoreRule.COMPLETE_COURSE);

    }

    @Override
    public void handleDailyComment(CreateCommentEvent event) {
        // TODO: 根据逻辑完成
        Long userId = null;

        earnCreditEventMapper.checkUserConsumeRecordHistory(userId);

        earnCreditEventMapper.insertDailyCommentHistory();
        earnCreditEventMapper.addUserMarketScore(userId, EarnCreditEventHandler.ScoreRule.DAILY_COMMENT);
    }
}
