package org.frosty.site_dispatch.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.sse.entity.SiteMessage;

import java.util.List;

@Mapper
public interface SSEIPMapper {

    @Select("select ip from user_ip where uid = #{toId}  ")
    List<String> findSSEIP(SiteMessage msg);
}
