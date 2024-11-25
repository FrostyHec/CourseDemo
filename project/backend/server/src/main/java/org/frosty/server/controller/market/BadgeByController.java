package org.frosty.server.controller.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.utils.Ex;
import org.frosty.server.entity.bo.market.BadgeInfo;
import org.frosty.server.services.market.BadgeByService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(PathConstant.API + "/market/badge")
@RequiredArgsConstructor
public class BadgeByController {
    private final BadgeByService badgeByService;

    @GetMapping("/my")
    public BadgeList getMyBadge(@GetPassedToken AuthInfo auth) {
        BadgeList badgeList = new BadgeList();
        badgeList.content = badgeByService.getMyBadge(auth.getUserID());
        return badgeList;
    }

    @PostMapping("/buy")
    public void buyBadge(@GetPassedToken AuthInfo auth, @RequestBody BadgeInfo badgeInfo) throws IOException {
        Long userId = auth.getUserID();
        badgeByService.buyBadge(userId, badgeInfo);
    }

    @GetMapping("/my-canbuy")
    public BadgeList getMyCanByBadge(@GetPassedToken AuthInfo auth) {
        // TODO 你看下 doc，30个硬编码的徽章你过滤掉已经买了的就是没买的,返回时忽略掉 name 和 market_score 段
        BadgeList badgeList = new BadgeList();
        badgeList.content = badgeByService.getMyCanByBadge(auth.getUserID());
        return badgeList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BadgeList {
        private List<BadgeInfo> content;
    }
}
