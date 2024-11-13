package org.frosty.sse_push.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.sse.entity.SiteMessage;
import org.frosty.sse.entity.UnackedSiteMessage;

import java.util.List;


@Mapper
public interface UnackedMapper extends BaseMapper<UnackedSiteMessage> {
    @Select("select * from msg_unacked where to_id=#{toId} ")
    List<SiteMessage> selectByToId(long toId);
}
