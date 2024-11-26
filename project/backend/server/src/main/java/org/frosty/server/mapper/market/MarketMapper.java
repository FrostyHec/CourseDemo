package org.frosty.server.mapper.market;


import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.market.MyMarketScore;

@Mapper
public interface MarketMapper {
    @Update("UPDATE market_score_record SET market_score = market_score + #{score} WHERE user_id = #{userId}")
    void addUserMarketScore(@Param("userId") Long userId, @Param("score") int score);


    @Select("SELECT * FROM market_score_record WHERE user_id = #{userID}")
    MyMarketScore selectByUserId(long userID);

    @Insert("INSERT INTO market_score_record (user_id, market_score) VALUES (#{userID},500)")
    void initialize(long userID);
}
