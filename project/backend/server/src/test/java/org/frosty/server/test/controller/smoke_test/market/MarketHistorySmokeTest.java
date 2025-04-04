package org.frosty.server.test.controller.smoke_test.market;

import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.market.MarketHistoryAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class MarketHistorySmokeTest {
    @Autowired
    private MarketHistoryAPI marketHistoryAPI;
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testHistoryCRUD() throws Exception {
        var name = "test";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token = res.first;

        // Get my history - initially empty
        var history = marketHistoryAPI.getMyHistorySuccess(token);
        assert history.isEmpty();

        // Simulate a consume record (depends on backend setup)
//        var consumeRecord = new ConsumeRecord()
//                .setActionType("BUY")
//                .setChangedScore(-50)
//                .setRemainScore(950);
        // Assume backend creates this record as part of another operation

        // Get my history - should include the new record
        history = marketHistoryAPI.getMyHistorySuccess(token);
        assert history.size() == 1;
        assert history.get(0).getChangedScore() == -50;

        // Additional operations and validations can be added as needed
    }
}

