package org.frosty.server.entity.bo.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadgeInfo {
    private Long userId;
    private Long badgeId;
    private String badgeName;
    private String image;//image url
    private Integer marketScore;
    private OffsetDateTime createdAt;
}