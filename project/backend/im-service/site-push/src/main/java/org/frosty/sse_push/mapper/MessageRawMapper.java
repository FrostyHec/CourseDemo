package org.frosty.sse_push.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.sse_push.entity.SingleMessageDTO;

import java.util.List;


@Mapper
public interface MessageRawMapper extends BaseMapper<SingleMessageDTO> {

    @Select("select * from msg where to_id=#{toId} ")
    List<SingleMessageDTO> selectByToId(long toId);
}
