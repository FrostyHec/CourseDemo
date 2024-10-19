package org.frosty.sse_push.mapper.wrapped;

import java.util.List;




import lombok.RequiredArgsConstructor;
import org.frosty.sse_push.annotation.DynamicTableNameMapper;
import org.frosty.sse_push.entity.SingleMessageDTO;
import org.frosty.sse_push.handler.DynamicTableNameType;
import org.frosty.sse_push.mapper.MessageRawMapper;

@DynamicTableNameMapper(type = DynamicTableNameType.MESSAGE_DTO, name = "unposed")
@RequiredArgsConstructor
public class UnposedMapper {
    private final MessageRawMapper mapper;

    public List<SingleMessageDTO> selectByToId(long uid) {
        return mapper.selectByToId(uid);
    }

    public boolean insertOrUpdate(SingleMessageDTO dto) {
        return mapper.insertOrUpdate(dto);
    }

    public int deleteByIds(List<SingleMessageDTO> unposeds) {
       return mapper.deleteByIds(unposeds);
    }
}
