package org.frosty.server.controller.market;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.entity.bo.market.action_type.ActionParam;
import org.frosty.server.entity.bo.market.action_type.BuyBadgeActionParam;
import org.frosty.server.services.market.MarketHistoryService;
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



//    @EqualsAndHashCode(callSuper = true)
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Accessors(chain = true)
//    @TableName("consume_record")
//    public class BadgeBuyRecord extends ConsumeRecord {
////
////        @Override
////        public BuyBadgeActionParam getActionParam() {
////            return (BuyBadgeActionParam) super.getActionParam();
////        }
////
////        @Override
////        public ConsumeRecord setActionParam(ActionParam actionParam) {
////            if (actionParam instanceof BuyBadgeActionParam) {
////                return super.setActionParam(actionParam);
////            } else {
////                throw new IllegalArgumentException("Action param must be of type BuyBadgeActionParam");
////            }
////        }
//    }







}
