package org.frosty.demo.services;


import org.frosty.demo.entity.dto.User;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password);
}
