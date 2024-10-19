package org.frosty.site_dispatch.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePacketDTO {
    List<SingleMessageDTO> unposed;
    List<SingleMessageDTO> unacked;
}
