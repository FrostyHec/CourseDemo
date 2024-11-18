package org.frosty.server.entity.bo.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadgeInfo {
    private Long userId;
    private Long badgeId;
    private String badgeName;
    private Integer marketScore;
}