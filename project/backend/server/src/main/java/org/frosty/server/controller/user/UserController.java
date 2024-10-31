package org.frosty.server.controller.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.User;
import org.frosty.server.services.user.UserService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 暂未实现用户名与密码的正则判断
    // 暂时认为注册阶段不需要修改用户信息
    @PostMapping("/user")
    public Response register(@RequestBody User user) {
        user.setUserId(null);
        // 使用passwordEncoder.encode对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 保存用户信息到数据库
        userService.insertUser(user);
        return Response.getSuccess("成功注册");
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

    @GetMapping("/user/search")
    public UserList searchUser(@RequestParam String firstName, @RequestParam String lastName,
                               @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        FrameworkUtils.notImplemented();
        throw new RuntimeException();
//        List<User> users = userService.searchByRealName(realName);
//        return Response.getSuccess(new UserList(users));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserList {
        private List<User> content;
    }
}
