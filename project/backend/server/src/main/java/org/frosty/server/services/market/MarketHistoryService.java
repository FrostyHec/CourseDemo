package org.frosty.server.services.market;

import org.frosty.server.entity.bo.market.ConsumeRecord;

import java.util.List;

public interface MarketHistoryService {
    List<ConsumeRecord> getHistoryByUserId(Long userId);

    void insertCourseCompleteHistory(ConsumeRecord consumeRecord);

    void insertDailyCommentHistory(ConsumeRecord consumeRecord);

    void insertConsumeRecord(ConsumeRecord consumeRecord);
}
