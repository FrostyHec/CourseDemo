package org.frosty.server.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

//    @Update("UPDATE users SET first_name=#{firstName}, last_name=#{lastName}, password=#{password}, role=#{role}, email=#{email}, updated_at=NOW() WHERE user_id=#{id}")
//    void updateUser(@Param("id") Long id, @Param("updatedUser") User updatedUser);
//
//    @Select("SELECT * FROM users WHERE user_id = #{id}")
//    User findUserById(Long id);
//
//    @Delete("DELETE FROM users WHERE user_id = #{id}")
//    void deleteUserById(Long id);

    @Select("SELECT user_id, first_name, last_name, role, email FROM users WHERE user_id = #{id}")
    User findPublicInfoById(Long id);
}

