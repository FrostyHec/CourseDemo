package org.frosty.server.services.market;

import org.frosty.server.entity.bo.market.MyMarketScore;

public interface MarketService {
    MyMarketScore getMyMarketScore(long userID);
}
