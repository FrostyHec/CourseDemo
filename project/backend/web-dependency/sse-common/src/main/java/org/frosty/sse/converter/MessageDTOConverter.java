package org.frosty.sse.converter;


import org.frosty.sse.entity.SingleMessageDTO;
import org.frosty.sse.entity.UnackedSingleMessageDTO;
import org.frosty.sse.entity.UnposedSingleMessageDTO;
import org.mapstruct.Mapper;

@Mapper
public interface MessageDTOConverter {
    UnposedSingleMessageDTO toUnposed(SingleMessageDTO singleMessageDTO);
    UnackedSingleMessageDTO toUnacked(SingleMessageDTO singleMessageDTO);
}
