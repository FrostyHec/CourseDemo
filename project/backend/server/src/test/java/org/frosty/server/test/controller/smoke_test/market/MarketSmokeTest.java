package org.frosty.server.test.controller.smoke_test.market;

import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.market.MarketAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class MarketSmokeTest {
    @Autowired
    private MarketAPI marketAPI;
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testMarketScore() throws Exception {
        var name = "test";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token = res.first;

        // Get my market score
        var myMarketScore = marketAPI.getMyMarketScoreSuccess(token);
        assert myMarketScore.getMarketScore() == 0; // Assuming initial score is 0

        // Additional operations and validations can be added as needed
    }
}
