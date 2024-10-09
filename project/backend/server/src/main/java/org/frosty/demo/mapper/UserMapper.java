package org.frosty.demo.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.demo.entity.dto.User;


@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUserName(String username);

    @Insert("INSERT INTO users(username, password) VALUES(#{username}, #{password})")
    void add(String username, String password);
}

