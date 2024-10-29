package org.frosty.server.services.user;


import org.frosty.server.entity.bo.User;
import org.frosty.server.event.delete_event.UserDeleteEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;



public interface UserService {


    // TODO example for event mechanism to handle delete
    void deleteUserById(Long userId);

    void publishDeleteUser(Long userId);


    void updateUser(Long id, User updatedUser);

    User findById(Long id);

    void deleteUser(Long id);

    User findPublicInfoById(Long id);


    List<User> searchByRealName(String realName);

    void handleUserDeleteEvent(UserDeleteEvent event);

    void insertUser(User user);//
}
