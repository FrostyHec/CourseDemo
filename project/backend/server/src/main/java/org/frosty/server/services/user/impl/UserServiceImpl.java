package org.frosty.server.services.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.User;
import org.frosty.server.event.delete_event.UserDeleteEvent;
import org.frosty.server.services.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void deleteUserById(Long userId) {

    }

    @Override
    public void publishDeleteUser(Long userId) {

    }


//    @Override
//    public void create(String username, String password) {
//
//    }

    @Override
    public void updateUser(Long id, User updatedUser) {

    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public User findPublicInfoById(Long id) {
        return null;
    }

    @Override
    public List<User> searchByRealName(String realName) {
        return List.of();
    }

    @Override
    @EventListener
    public void handleUserDeleteEvent(UserDeleteEvent event) {
        log.info("user_deleted");
    }

}
