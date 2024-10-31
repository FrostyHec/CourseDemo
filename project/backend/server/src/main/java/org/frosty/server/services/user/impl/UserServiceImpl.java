package org.frosty.server.services.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    private final UserMapper userMapper;

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


    // TODO
//    @Override
//    public List<User> searchByRealName(String firstName, String lastName,int pageNum, int pageSize) {
//        return null;
//    }
    @Override
    public List<User> searchByRealName(String firstName, String lastName, int pageNum, int pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 构建查询条件
        if (firstName != null && !firstName.isEmpty()) {
            queryWrapper.eq("first_name", firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            queryWrapper.eq("last_name", lastName);
        }

        List<User> users;

        // 检查 pageSize 是否为 -1，如果是则查询所有记录
        if (pageSize == -1) {
            users = userMapper.selectList(queryWrapper);
        } else {
            Page<User> page = new Page<>(pageNum, pageSize);
            Page<User> selectedPage = userMapper.selectPage(page, queryWrapper);
            users = selectedPage.getRecords();
        }

        return users;
    }



    // TODO
    @Override
    @EventListener
    public void handleUserDeleteEvent(UserDeleteEvent event) {
        log.info("user_deleted");
    }


}
