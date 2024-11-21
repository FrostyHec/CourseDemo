package org.frosty.server.mapper.market;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.market.ConsumeRecord;

import java.util.List;

@Mapper
public interface MarketHistoryMapper {
    @Select("SELECT * FROM consume_record WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<ConsumeRecord> getHistoryByUserId(@Param("userId") Long userId);
}
