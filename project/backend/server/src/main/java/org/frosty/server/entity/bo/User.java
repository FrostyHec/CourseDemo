package org.frosty.server.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private UserType role;
    private OffsetDateTime createAt;
    private OffsetDateTime updateAt;

    public enum UserType {
        admin,
        teacher,
        student
    }
}
