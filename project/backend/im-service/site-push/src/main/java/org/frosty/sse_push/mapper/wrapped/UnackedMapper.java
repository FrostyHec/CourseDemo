package org.frosty.sse_push.mapper.wrapped;


import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.BatchResult;
import org.frosty.sse_push.annotation.DynamicTableNameMapper;
import org.frosty.sse_push.entity.SingleMessageDTO;
import org.frosty.sse_push.handler.DynamicTableNameType;
import org.frosty.sse_push.mapper.MessageRawMapper;

import java.util.List;

@DynamicTableNameMapper(type = DynamicTableNameType.MESSAGE_DTO, name = "unacked")
@RequiredArgsConstructor
public class UnackedMapper {
    private final MessageRawMapper mapper;
    public List<SingleMessageDTO> selectByToId(long uid) {
        return mapper.selectByToId(uid);
    }

    public List<BatchResult> insert(List<SingleMessageDTO> requiredAckList) {
        return mapper.insert(requiredAckList);
    }

    public int insert(SingleMessageDTO dto){
        return mapper.insert(dto);
    }

    public int deleteByIds(List<SingleMessageDTO> unposeds) {
        return mapper.deleteByIds(unposeds);
    }
}
