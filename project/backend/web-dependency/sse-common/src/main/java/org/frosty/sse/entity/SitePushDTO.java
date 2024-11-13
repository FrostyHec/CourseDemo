package org.frosty.sse.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SitePushDTO {
    PushType pushType;
    Object body;

    public SitePushDTO(SiteMessage siteMessage) {
        pushType = PushType.SINGLE;
        body = siteMessage;
    }

    public SitePushDTO(SiteMessagePacketDTO siteMessagePacketDTO) {
        pushType = PushType.PACKET;
        body = siteMessagePacketDTO;
    }
    public enum PushType {
        SINGLE(1),
        PACKET(2);
        @EnumValue
        @JsonValue
        private final int value;

        PushType(int value) {
            this.value = value;
        }
    }

}
