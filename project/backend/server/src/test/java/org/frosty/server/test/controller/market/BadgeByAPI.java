package org.frosty.server.test.controller.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.entity.bo.market.BadgeInfo;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BadgeByAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String badgeBaseUrl = PathConstant.API + "/market/badge";

    public ResultActions getMyBadge(String token) throws Exception {
        String url = badgeBaseUrl + "/my";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<BadgeInfo> getMyBadgeSuccess(String token) throws Exception {
        var resp = getMyBadge(token)
                .andExpect(RespChecker.success())
                .andReturn();

        // 解析响应为 BadgeList
        var badgeList = JsonUtils.toObject(resp, BadgeByController.BadgeList.class);

        // 安全处理：如果 badgeList 或内容为空，返回空列表
        if (badgeList == null || badgeList.getContent() == null) {
            return Collections.emptyList();
        }

        return badgeList.getContent();
    }


    public ResultActions buyBadge(String token, BadgeInfo badgeInfo) throws Exception {
        String url = badgeBaseUrl + "/buy";
        String json = objectMapper.writeValueAsString(badgeInfo);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void buyBadgeSuccess(String token, BadgeInfo badgeInfo) throws Exception {
        buyBadge(token, badgeInfo).andExpect(RespChecker.success());
    }

    public ResultActions getMyCanBuyBadge(String token) throws Exception {
        String url = badgeBaseUrl + "/my-canbuy";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<BadgeInfo> getMyCanBuyBadgeSuccess(String token) throws Exception {
        var resp = getMyCanBuyBadge(token)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, BadgeByController.BadgeList.class).getContent();
    }
}

