package org.frosty.server.controller.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.services.market.MarketHistoryService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API + "/market/history")
@RequiredArgsConstructor
public class MarketHistoryController {

    private final MarketHistoryService marketHistoryService;

    @GetMapping("/history/my")
    public ConsumeRecordList getMyHistory(@GetPassedToken AuthInfo auth){
        Long userId = auth.getUserID();
        List<ConsumeRecord> records = marketHistoryService.getHistoryByUserId(userId);
        return new ConsumeRecordList(records);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConsumeRecordList{
        private List<ConsumeRecord> content;
    }

}
