package org.frosty.sse_push.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

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
