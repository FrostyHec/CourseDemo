package org.frosty.common_service.im.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteMessage {
    private Long messageId;
    private long fromId;
    private long toId;
    private MessageType type;
    private boolean requiredAck;
    private JsonNode body;

    @Getter
    public enum MessageType {
        NEW(1),
        UPDATE(2),
        DELETE(3);
        @EnumValue
        @JsonValue
        private final int value;

        MessageType(int value) {
            this.value = value;
        }
    }
}
