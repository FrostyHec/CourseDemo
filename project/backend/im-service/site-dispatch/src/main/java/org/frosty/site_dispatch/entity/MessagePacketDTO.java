package org.frosty.site_dispatch.entity;

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
