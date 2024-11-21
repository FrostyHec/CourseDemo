package org.frosty.server.services.market;

import org.frosty.server.entity.bo.market.ConsumeRecord;

import java.util.List;

public interface MarketHistoryService {
    List<ConsumeRecord> getHistoryByUserId(Long userId);
}
