package org.frosty.server.controller.market;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.services.market.BadgeByService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API + "/market/badge")
@RequiredArgsConstructor
public class BadgeByController {
    private final BadgeByService badgeByService;
    @GetMapping("/my")
    public BadgeList getMyBadge(@GetPassedToken AuthInfo auth){
        return badgeByService.getMyBadge();
    }

    @GetMapping("/buy")
    public void buyBadge(@GetPassedToken AuthInfo auth, BadgeInfo badgeInfo ){
        badgeByService.buyBadge(badgeInfo);
    }

    @GetMapping("/my-canbuy")
    public BadgeList getMyCanByBadge(@GetPassedToken AuthInfo auth,BadgeInfo badgeInfo ){
        return badgeByService.getMyCanByBadge(badgeInfo);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BadgeList{
        private List<BadgeInfo> content;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BadgeInfo{
        private Long badgeId;
        private String badgeName;
        private Integer marketScore;
    }
}
