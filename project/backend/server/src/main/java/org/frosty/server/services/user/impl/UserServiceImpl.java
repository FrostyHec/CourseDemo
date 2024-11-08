package org.frosty.server.services.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.converter.CommonConverter;
import org.frosty.server.entity.po.UserPublicInfo;
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
    private final UserMapper userMapper;
    private final CommonConverter converter;

    @Override
    public void deleteUserById(Long userId) {

    }

    @Override
    public void publishDeleteUser(Long userId) {

    }

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
        return userMapper.selectById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public User findPublicInfoById(Long id) {
        return userMapper.findPublicInfoById(id);
    }

    @Override
    public List<User> searchByRealName(String firstName, String lastName, int pageNum, int pageSize) {
        return List.of();
    }


//    // TODO
//    @Override
//    public List<User> searchByRealName(String realName) {
//        return userMapper.searchByRealName(realName);
//    }

    @Override
    public List<UserPublicInfo> searchUser(String firstName, String lastName, int pageNum, int pageSize) {

        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.like("first_name", firstName).like("last_name", lastName);
        if(pageSize != -1) {
            queryWrapper.last("LIMIT " + pageSize + " OFFSET " + (pageNum - 1) * pageSize);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(converter::toUserPublicInfo).toList();
    }

    // TODO
    @Override
    @EventListener
    public void handleUserDeleteEvent(UserDeleteEvent event) {
        log.info("user_deleted");
    }





}
