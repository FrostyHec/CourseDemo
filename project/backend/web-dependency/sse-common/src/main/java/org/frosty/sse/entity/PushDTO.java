package org.frosty.sse.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
