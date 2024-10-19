package org.frosty.site_dispatch.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import org.frosty.site_dispatch.entity.SingleMessageDTO;

@Mapper
public interface SSEIPMapper {

    @Select("select ip from user_ip where uid = #{toId}  ")
    String findSSEIP(SingleMessageDTO msg);
}
