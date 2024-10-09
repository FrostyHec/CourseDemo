package org.frosty.demo.services.impl;


import org.frosty.demo.entity.dto.User;
import org.frosty.demo.mapper.UserMapper;
import org.frosty.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public void register(String username, String password) {
        userMapper.add(username,password);
    }
}
