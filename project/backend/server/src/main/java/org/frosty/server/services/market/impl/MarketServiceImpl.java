package org.frosty.server.services.market.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.market.MyMarketScore;
import org.frosty.server.mapper.market.MarketMapper;
import org.frosty.server.services.market.MarketService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final MarketMapper marketMapper;
    @Override
    public MyMarketScore getMyMarketScore(long userID) {
        return marketMapper.selectByUserId(userID);
    }
}
