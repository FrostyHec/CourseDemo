package org.frosty.common_service.message_push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteMessage {
    Long srcUid;
    Long targetUid;
}
