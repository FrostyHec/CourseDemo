package org.frosty.sse.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.frosty.sse.constant.MessageBodyType;

import static org.frosty.sse.constant.MessageCommonConstant.systemMessageFromId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteMessage {
    protected Long messageId;// if msg type is new, the messageId field will be ignored for creation
    protected long fromId;
    protected long toId;
    protected MessageType type;
    protected MessageBodyType bodyType;
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
    public static SiteMessage getSimpleSystemNewMessage(long toId, MessageBodyType bodyType, JsonNode body){
        return new SiteMessage(null,systemMessageFromId,toId,MessageType.NEW,bodyType,false,body);
    }
    public static SiteMessage getSimpleNewMessage(long fromId,long toId,MessageBodyType bodyType,JsonNode body){
        return new SiteMessage(null,fromId,toId,MessageType.NEW,bodyType,false,body);
    }
}
