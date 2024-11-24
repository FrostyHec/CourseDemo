package org.frosty.server.mapper.market;


import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.market.MyMarketScore;

@Mapper
public interface MarketMapper {
    @Update("""
                INSERT INTO market_score_record (user_id, market_score)
                VALUES (#{userId}, #{scoreRule.score})
                ON CONFLICT (user_id)
                DO UPDATE SET market_score = market_score + #{scoreRule.score}
            """)
    void addUserMarketScore(@Param("userId") Long userId, @Param("scoreRule") int scoreRule);

    @Select("SELECT * FROM market_score_record WHERE user_id = #{userID}")
    MyMarketScore selectByUserId(long userID);

    @Insert("INSERT INTO market_score_record (user_id, market_score) VALUES (#{userId}, 0)")
    void initialize(long userID);
}
