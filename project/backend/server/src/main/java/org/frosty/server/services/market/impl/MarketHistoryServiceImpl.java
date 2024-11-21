package org.frosty.server.services.market.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.mapper.market.MarketHistoryMapper;
import org.frosty.server.services.market.MarketHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketHistoryServiceImpl implements MarketHistoryService {
    private final MarketHistoryMapper marketHistoryMapper;
    @Override
    public List<ConsumeRecord> getHistoryByUserId(Long userId) {
        return marketHistoryMapper.getHistoryByUserId(userId);
    }
}
