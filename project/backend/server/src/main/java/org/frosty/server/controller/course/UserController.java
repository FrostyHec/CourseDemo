package org.frosty.server.controller.course;


import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.User;
import org.frosty.server.services.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(    PathConstant.API + "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 暂未实现密码加密（可以用passwordEncoder）
    // 暂未实现用户名与密码的正则判断
    // 暂时认为注册阶段不需要修改用户信息
    @PostMapping
    public Map<String, String> register(String username, String password, String confirmPassword) {
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
        throw new RuntimeException("Not implemented");// TODO
    }

//    @PostMapping("/login")
//    public Map<String, String> login(String username, String password) {
//        User user = userService.findByUserName(username);
//        if (user == null) {
//            return Map.of("message", "用户名或密码错误");
//        }
//        if (!user.getPassword().equals(password)) {
//            return Map.of("message", "用户名或密码错误");
//        } else {
//            return Map.of("message", "成功登录");
//        }
//    }
}
