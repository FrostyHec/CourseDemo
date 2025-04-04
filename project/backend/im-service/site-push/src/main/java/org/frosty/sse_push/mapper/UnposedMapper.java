package org.frosty.sse_push.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.sse.entity.SiteMessage;
import org.frosty.sse.entity.UnposedSiteMessage;

import java.util.List;

@Mapper
public interface UnposedMapper  extends BaseMapper<UnposedSiteMessage>{
    @Select("select * from msg_unposed where to_id=#{toId} ")
    List<SiteMessage> selectByToId(long toId);
}
