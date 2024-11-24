package org.frosty.server.test.controller.smoke_test.market;

import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.bo.market.BadgeInfo;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.market.BadgeByAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class BadgeBySmokeTest {
    @Autowired
    private BadgeByAPI badgeByAPI;
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testBasicOperations() throws Exception {
        var name = "test";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token = res.first;

        // Get my badges - initially empty
        var myBadges = badgeByAPI.getMyBadgeSuccess(token);
        assert myBadges.isEmpty();

        // Attempt to buy a badge
        var badgeInfo = new BadgeInfo();
        badgeInfo.setBadgeId(1L);
        badgeInfo.setBadgeName("Test Badge");
        badgeInfo.setMarketScore(100);


        badgeByAPI.buyBadgeSuccess(token, badgeInfo);

        // Get my badges - should include the bought badge
        myBadges = badgeByAPI.getMyBadgeSuccess(token);
        assert myBadges.size() == 1;
        assert myBadges.get(0).getBadgeId().equals(1L);

        // Get badges available to buy - should exclude the already bought badge
        var canBuyBadges = badgeByAPI.getMyCanBuyBadgeSuccess(token);
        assert canBuyBadges.stream().noneMatch(b -> b.getBadgeId().equals(1L));
    }
}
