package org.frosty.server.mapper.market;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.market.MyMarketScore;

@Mapper
public interface MarketMapper {
    @Select("SELECT * FROM market_score_record WHERE user_id = #{userID}")
    MyMarketScore selectByUserId(long userID);
}
