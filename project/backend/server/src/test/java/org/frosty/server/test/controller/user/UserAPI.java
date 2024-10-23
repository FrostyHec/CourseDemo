package org.frosty.server.test.controller.user;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAPI {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public User addSimpleTestUser(String firstName, String password, User.Role role) {

        var user = new User()
                .setFirstName(firstName)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role)
                .setEmail(firstName + "@test.com");
        return addTestUser(user);
    }

    public User addTestUser(User user) {
        mapper.insert(user);
        return user;
    }

    public void checkPublicUserEquality(User origin, UserPublicInfo info) {
        assert origin.getUserId().equals(info.getUserId());
        assert origin.getFirstName().equals(info.getFirstName());
        assert origin.getLastName() == null || origin.getLastName().equals(info.getLastName());
        assert origin.getRole().equals(info.getRole());
        assert origin.getEmail().equals(info.getEmail());
    }

}
