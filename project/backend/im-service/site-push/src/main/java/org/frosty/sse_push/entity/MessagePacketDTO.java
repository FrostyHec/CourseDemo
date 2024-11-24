package org.frosty.sse_push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePacketDTO {
    List<SingleMessageDTO> unposed;
    List<SingleMessageDTO> unacked;
}
