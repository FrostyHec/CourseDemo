package org.frosty.sse_push.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.sse.entity.SingleMessageDTO;
import org.frosty.sse.entity.UnposedSingleMessageDTO;

import java.util.List;

@Mapper
public interface UnposedMapper  extends BaseMapper<UnposedSingleMessageDTO>{
    @Select("select * from msg_unposed where to_id=#{toId} ")
    List<SingleMessageDTO> selectByToId(long toId);
}
