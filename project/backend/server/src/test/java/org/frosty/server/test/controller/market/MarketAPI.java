package org.frosty.server.test.controller.market;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.market.MyMarketScore;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@RequiredArgsConstructor
public class MarketAPI {
    private final MockMvc mockMvc;
    private final AuthUtil authUtil;
    private final String marketBaseUrl = PathConstant.API + "/market";

    public ResultActions getMyMarketScore(String token) throws Exception {
        String url = marketBaseUrl + "/my-market-score";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public MyMarketScore getMyMarketScoreSuccess(String token) throws Exception {
        var resp = getMyMarketScore(token)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, MyMarketScore.class);
    }
}

