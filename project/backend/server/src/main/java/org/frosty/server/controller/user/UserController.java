package org.frosty.server.controller.user;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.User;

import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.services.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(    PathConstant.API)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 暂未实现密码加密（可以用passwordEncoder）
    // 暂未实现用户名与密码的正则判断
    // 暂时认为注册阶段不需要修改用户信息
    @PostMapping("/user")
    public void register(@RequestBody User user) {
//        if (!password.equals(confirmPassword)) {
//            return Map.of("message", "密码不一致");
//        }
//        User user = userService.findByUserName(username);
//        if (user == null) {
//            userService.create(username, password);
//            return Map.of("message", "成功注册");
//        } else {
//            return Map.of("message", "用户名被占用");
//        }
        userService.insertUser(user);

        throw new RuntimeException("Not implemented");// TODO
    }

    // 修改用户全部资料
    @PutMapping("/user/{id}")
    public Response updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        updatedUser.setUserId(id);
        userService.updateUser(id, updatedUser);
        return Response.getSuccess("User updated successfully.");
    }

    // 获取用户全部资料
    @GetMapping("/user/{id}")
    public Response getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return Response.getSuccess(user);
    }

    // 删除用户
    @DeleteMapping("/user/{id}")
    public Response deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Response.getSuccess("User deleted successfully.");
    }

    // 查询单个用户公开信息
    @GetMapping("/user/public/{id}")
    public Response getUserPublicInfo(@PathVariable Long id) {
        User user = userService.findPublicInfoById(id);
        return Response.getSuccess(user);
    }

    // 模糊搜索用户（基于用户实名）
    // TODO
    @GetMapping("/user/search")
    public List<UserPublicInfo> searchUser(@RequestParam String realName) {
        throw new NotImplementedException();
//        List<User> users = userService.searchByRealName(realName);
//        return Response.getSuccess(users);
    }
}
