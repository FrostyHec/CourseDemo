package org.frosty.server.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String firstName;
    private String lastName;//can omit
    private String password;
    private String confirmPassword;
    private Role role;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String email;

    // 大小写敏感
    public enum Role {
        admin,
        teacher,
        student
    }
}
