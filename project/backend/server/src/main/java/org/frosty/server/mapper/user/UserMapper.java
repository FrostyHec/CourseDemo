package org.frosty.server.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.User;

import java.util.List;

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

    /**
     * 根据输入的关键词在用户的姓名（first_name 和 last_name）中进行搜索，
     * 查找包含所有指定关键词的用户。
     * 此方法使用 PostgreSQL 的 `to_tsvector` 和 `to_tsquery` 函数进行全文搜索，
     * 确保在 first_name 或 last_name 字段中都包含所有提供的关键词。
     *
     * <p>示例：如果输入 {@code realName} 为 "Alice Smith"，则方法将返回包含
     * "Alice" 和 "Smith" 的用户，不论顺序，例如 "Alice Smith"、"Smith Alice"，
     * 或 "Alice Johnson-Smith"。</p>
     *
     * @param realName 一个包含多个以空格分隔的关键词的字符串，用于在用户的姓名中查找。
     *                 例如 "Alice Smith"。
     * @return 符合搜索条件的 {@link User} 对象列表，其中每个用户的 first_name 或 last_name
     * 字段包含所有指定的关键词。
     */
    @Select("""
            SELECT * FROM users 
            WHERE to_tsvector('simple', first_name || ' ' || last_name) @@ to_tsquery('simple', REPLACE(#{realName}, ' ', ' & '))
            """)
    List<User> searchByRealName(String realName);


    @Select("SELECT * FROM users WHERE email = #{email}")
    User selectByEmail(String email);
}

