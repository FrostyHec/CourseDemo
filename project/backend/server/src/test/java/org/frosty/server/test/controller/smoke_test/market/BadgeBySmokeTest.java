package org.frosty.server.test.controller.smoke_test.market;

import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.bo.market.BadgeInfo;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.market.BadgeByAPI;
import org.frosty.server.test.controller.market.MarketAPI;
import org.frosty.server.test.controller.market.MarketHistoryAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.frosty.server.entity.bo.market.ConsumeRecord.ConsumeActionType.buy_badge;

@IdempotentControllerTest
public class BadgeBySmokeTest {
    @Autowired
    private BadgeByAPI badgeByAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private MarketAPI marketAPI;
    @Autowired
    private MarketHistoryAPI marketHistoryAPI;

    @Test
    public void testBasicOperations() throws Exception {
        var name = "test";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token = res.first;

//        // Get my badges - initially empty
//        assert badgeByAPI.getMyBadgeSuccess(token) == null;
////        assert myBadges.isEmpty();

        // Attempt to buy a badge
        var badgeInfo = new BadgeInfo();
        badgeInfo.setBadgeId(1L);
        badgeInfo.setBadgeName("Test Badge");
        badgeInfo.setMarketScore(100);
        badgeInfo.setUserId(res.second.getUserId());
        badgeInfo.setImage("aaa");



        // Get my market score
        var myMarketScore = marketAPI.getMyMarketScoreSuccess(token);
        assert myMarketScore.getMarketScore() == 500; // Assuming initial score is 500

        badgeByAPI.buyBadgeSuccess(token, badgeInfo);
        var History = marketHistoryAPI.getMyHistorySuccess(token);

        System.out.println("-----------------------");
        System.out.println(History);
        System.out.println("-----------------------");

        assert History.size() == 1;
        assert History.get(0).getActionType().equals(buy_badge);

        // Get my badges - should include the bought badge
        var myBadges = badgeByAPI.getMyBadgeSuccess(token);


        assert myBadges.size() == 1;
        assert myBadges.get(0).getBadgeId().equals(1L);

        // Get badges available to buy - should exclude the already bought badge
        var canBuyBadges = badgeByAPI.getMyCanBuyBadgeSuccess(token);
        assert canBuyBadges.size() == 29;
//        assert canBuyBadges.stream().noneMatch(b -> b.getBadgeId().equals(1L));
    }
}
