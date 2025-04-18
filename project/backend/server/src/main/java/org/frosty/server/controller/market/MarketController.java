package org.frosty.server.controller.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.market.MyMarketScore;
import org.frosty.server.services.market.MarketService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(PathConstant.API + "/market")
@RequiredArgsConstructor
public class MarketController {
    private final MarketService marketService;

    @GetMapping("/my-market-score")
    public MyMarketScore getMyMarketScore(@GetPassedToken AuthInfo auth){
        return marketService.getMyMarketScore(auth.getUserID());
    }
}
