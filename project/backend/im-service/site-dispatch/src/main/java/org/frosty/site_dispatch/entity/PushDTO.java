package org.frosty.site_dispatch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PushDTO {
    PushType pushType;
    Object body;

    public PushDTO(SingleMessageDTO singleMessageDTO) {
        pushType = PushType.SINGLE;
        body = singleMessageDTO;
    }

    public PushDTO(MessagePacketDTO messagePacketDTO) {
        pushType = PushType.PACKET;
        body = messagePacketDTO;
    }
}
