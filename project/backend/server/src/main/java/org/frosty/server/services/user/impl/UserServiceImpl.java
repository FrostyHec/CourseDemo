package org.frosty.server.services.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.User;
import org.frosty.server.event.delete_event.UserDeleteEvent;
import org.frosty.server.mapper.user.UserMapper;
import org.frosty.server.services.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ApplicationEventPublisher eventPublisher;



    @Override
    public void deleteUserById(Long userId) {

    }

    @Override
    public void publishDeleteUser(Long userId) {

    }

    private final UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        userMapper.insert(user);//
    }

    @Override
    public void updateUser(Long id, User updatedUser) {
        userMapper.updateById(updatedUser);
    }

    @Override
    public User findById(Long id) {
        User user = userMapper.selectById(id);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public User findPublicInfoById(Long id) {
        User userPublicInfo = userMapper.findPublicInfoById(id);
        return userPublicInfo;
    }


    // TODO
    @Override
    public List<User> searchByRealName(String realName) {
        return List.of();
    }


    // TODO
    @Override
    @EventListener
    public void handleUserDeleteEvent(UserDeleteEvent event) {
        log.info("user_deleted");
    }



}
