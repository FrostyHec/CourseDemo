package org.frosty.sse_push.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.sse.entity.SingleMessageDTO;
import org.frosty.sse.entity.UnackedSingleMessageDTO;

import java.util.List;


@Mapper
public interface UnackedMapper extends BaseMapper<UnackedSingleMessageDTO> {
    @Select("select * from msg_unacked where to_id=#{toId} ")
    List<SingleMessageDTO> selectByToId(long toId);
}
