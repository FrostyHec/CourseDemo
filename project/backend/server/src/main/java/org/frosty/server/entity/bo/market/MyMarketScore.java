package org.frosty.server.entity.bo.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMarketScore{
    private Long userId;
    private Integer marketScore;
}