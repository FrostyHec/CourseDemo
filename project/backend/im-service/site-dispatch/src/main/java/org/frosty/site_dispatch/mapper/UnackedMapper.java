package org.frosty.site_dispatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.sse.entity.SiteMessage;
import org.frosty.sse.entity.UnackedSiteMessage;

@Mapper
public interface UnackedMapper extends BaseMapper<UnackedSiteMessage> {

    @Update("delete from msg_unacked where message_id = #{messageId} ")
    void deleteIfExists(SiteMessage msg);

    @Update("delete from msg_unacked where message_id = #{mid}")
    void deleteById(long mid);

    @Select("select * from msg_unacked where message_id = #{mid}")
    SiteMessage selectById(long mid);
}
