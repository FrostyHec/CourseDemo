package org.frosty.sse_push.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.frosty.sse.entity.SSEIPEntity;


@Mapper
public interface SSEIPMapper {
    @Insert("insert into user_ip (uid, ip) values (#{uid}, #{ip}) on conflict do nothing ")
    void insertIfNotExist(SSEIPEntity sseipEntity);

    @Insert("delete from user_ip where uid = #{uid}")
    void delete(long uid);
}
