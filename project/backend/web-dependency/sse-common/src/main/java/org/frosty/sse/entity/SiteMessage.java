package org.frosty.sse.entity;

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
    protected Long messageId;// if msg type is new, the messageId field will be ignored for creation
    protected long fromId;
    protected long toId;
    protected MessageType type;
    protected boolean requiredAck;
    protected JsonNode body;

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
