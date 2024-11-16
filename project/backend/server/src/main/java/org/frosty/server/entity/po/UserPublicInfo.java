package org.frosty.server.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frosty.server.entity.bo.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicInfo {
    Long userId;
    String firstName;
    String lastName;
    User.Role role;
    String email;
}
