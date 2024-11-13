package org.frosty.sse.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteMessagePacketDTO {
    List<SiteMessage> unposed;
    List<SiteMessage> unacked;
}
