package org.frosty.server.controller.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.services.user.UserService;
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
        UserPublicInfo user = userService.findPublicInfoById(id);
        return Response.getSuccess(user);
    }

    @GetMapping("/user/search")
    public UserList searchUser(@RequestParam String first_name, @RequestParam String last_name,
                               @RequestParam Integer page_num, @RequestParam Integer page_size) {
        if (page_num < 0 || page_size < -1) {
            throw new RuntimeException("Invalid page number or page size.");
            //return new UserList(List.of());
        }
        List<UserPublicInfo> users = userService.searchUser(first_name, last_name, page_num, page_size);
        return new UserList(users);
    }

    // 精确搜索用户（基于用户邮箱）
    @GetMapping("/user/search/{email}")
    public Response getUserByEmail(@PathVariable String email) {
        UserPublicInfo user = userService.findPublicInfoByEmail(email);
        return Response.getSuccess(user);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserList {
        private List<UserPublicInfo> content;
    }
}
