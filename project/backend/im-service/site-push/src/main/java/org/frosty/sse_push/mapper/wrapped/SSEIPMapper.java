package org.frosty.sse_push.mapper.wrapped;




import lombok.RequiredArgsConstructor;
import org.frosty.sse_push.annotation.DynamicTableNameMapper;
import org.frosty.sse_push.entity.SSEIPEntity;
import org.frosty.sse_push.mapper.SSEIPRawMapper;


@DynamicTableNameMapper
@RequiredArgsConstructor
public class SSEIPMapper{
    private final SSEIPRawMapper mapper;

    public boolean insertOrUpdate(SSEIPEntity sseipEntity) {
        return mapper.insertOrUpdate(sseipEntity);
    }

    public int deleteById(long uid) {
        return mapper.deleteById(uid);
    }
}
