package org.frosty.server.services.user;


import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.event.delete_event.UserDeleteEvent;

import java.util.List;


public interface UserService {


    // TODO example for event mechanism to handle delete
    void deleteUserById(Long userId);

    void publishDeleteUser(Long userId);





    void updateUser(Long id, User updatedUser);

    User findById(Long id);

    void deleteUser(Long id);

    UserPublicInfo findPublicInfoById(Long id);


    List<User> searchByRealName(String firstName, String lastName,int pageNum, int pageSize);

    void handleUserDeleteEvent(UserDeleteEvent event);

    void insertUser(User user);//

    List<UserPublicInfo> searchUser(String firstName, String lastName, int pageNum, int pageSize);

    UserPublicInfo findPublicInfoByEmail(String email);
}
