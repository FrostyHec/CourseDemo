package org.frosty.server.mapper.market;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.controller.market.BadgeByController;
import org.frosty.server.entity.bo.market.BadgeInfo;

@Mapper
public interface BadgeByMapper extends BaseMapper<BadgeInfo> {
    @Select("SELECT * FROM badge_record WHERE user_id = #{userID}")
    BadgeByController.BadgeList selectMyBadge(long userID);

    @Insert("""
                INSERT INTO badge_record (
                    user_id, badge_id, badge_name, market_score
                ) VALUES (
                    #{userId}, #{badgeId}, #{badgeName}, #{marketScore}
                )
                ON CONFLICT (user_id, badge_id)
                DO NOTHING
            """)
    void insertBadge(BadgeInfo badgeInfo);


    @Select("""
                WITH all_badges AS (
                    SELECT generate_series(1, 30) AS badge_id
                )
                SELECT
                    #{userID} AS user_id, -- 将 userID 直接拼接到查询中
                    all_badges.badge_id
                FROM all_badges
                WHERE all_badges.badge_id NOT IN (
                    SELECT badge_id
                    FROM badge_record
                    WHERE user_id = #{userID}
                )
            """)
    BadgeByController.BadgeList selectMyCanByBadge(long userID);
}
