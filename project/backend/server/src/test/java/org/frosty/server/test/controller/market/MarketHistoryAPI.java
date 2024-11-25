package org.frosty.server.test.controller.market;


import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.market.MarketHistoryController;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MarketHistoryAPI {
    private final MockMvc mockMvc;
    private final AuthUtil authUtil;
    private final String historyBaseUrl = PathConstant.API + "/market/history";

    public ResultActions getMyHistory(String token) throws Exception {
        String url = historyBaseUrl + "/history/my";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<ConsumeRecord> getMyHistorySuccess(String token) throws Exception {
        var resp = getMyHistory(token)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, MarketHistoryController.ConsumeRecordList.class).getContent();
    }
}

