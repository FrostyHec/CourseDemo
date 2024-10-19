package org.frosty.server.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.frosty.server.entity.bo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

