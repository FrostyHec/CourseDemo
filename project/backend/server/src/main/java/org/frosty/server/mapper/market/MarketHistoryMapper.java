package org.frosty.server.mapper.market;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.market.ConsumeRecord;

import java.util.List;

@Mapper
public interface MarketHistoryMapper extends BaseMapper<ConsumeRecord>{
    @Insert("""
                INSERT INTO consume_record (
                    user_id,  changed_score, remain_score
                ) VALUES (
                    #{userId},  #{changedScore}, #{remainScore}
                )
            """)
    void insertHistory(ConsumeRecord consumeRecord);


    @Select("SELECT * FROM consume_record WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<ConsumeRecord> getHistoryByUserId(@Param("userId") Long userId);

}
