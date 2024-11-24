package org.frosty.server.services.market.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.market.EarnCreditEventHandler;
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
        MyMarketScore myMarketScore = marketMapper.selectByUserId(userID);
        if (myMarketScore == null) {
            marketMapper.initialize(userID);
            myMarketScore = marketMapper.selectByUserId(userID);
        }
        return myMarketScore;
    }

    @Override
    public void addUserMarketScore(Long userId, int score) {
        marketMapper.addUserMarketScore(userId, score);
    }
}
