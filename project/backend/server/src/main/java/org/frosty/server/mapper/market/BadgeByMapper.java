package org.frosty.server.mapper.market;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.controller.market.BadgeByController;

@Mapper
public interface BadgeByMapper{
    @Select("")
    BadgeByController.BadgeList selectMyBadge();

    @Insert("")
    void insertBadge(BadgeByController.BadgeInfo badgeInfo);

    @Select("")
    BadgeByController.BadgeList selectMyCanByBadge();
}
