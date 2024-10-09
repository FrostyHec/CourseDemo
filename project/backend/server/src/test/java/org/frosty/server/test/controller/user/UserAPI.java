package org.frosty.server.test.controller.user;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.User;
import org.frosty.server.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAPI {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public User addTestUser(String name, String password, User.Role role) {

        var user = new User()
                .setUsername(name)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role);
        return addTestUser(user);
    }

    public User addTestUser(User user) {
        mapper.insert(user);
        return user;
    }

}
