package org.frosty.server.entity.bo.market.action_type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyBadgeActionParam implements ActionParam{
    private Long badgeId;
    private String badgeName;
}
