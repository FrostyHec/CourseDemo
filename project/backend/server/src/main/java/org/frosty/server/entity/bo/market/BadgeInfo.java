package org.frosty.server.entity.bo.market;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("badge_record")
public class BadgeInfo {
    private Long userId;
    private Long badgeId;
    private String badgeName;
    private String image;//image url
    private Integer marketScore;
    private OffsetDateTime createdAt;
}