package org.frosty.server.mapper.market;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.server.controller.market.EarnCreditEventHandler;

@Mapper
public interface EarnCreditEventMapper {
    @Insert("")
    void insertCourseCompleteHistory();


    @Insert("")
    void insertDailyCommentHistory();


    @Update("")
    void addUserMarketScore(Long userId, EarnCreditEventHandler.ScoreRule scoreRule);

    @Select("")
    void checkUserConsumeRecordHistory(Long userId);
}
