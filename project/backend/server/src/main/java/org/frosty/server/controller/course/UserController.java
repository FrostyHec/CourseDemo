package org.frosty.demo.controller;


import lombok.RequiredArgsConstructor;
import org.frosty.demo.config.CommonConstant;
import org.frosty.demo.entity.dto.User;
import org.frosty.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(CommonConstant.API_VERSION + "/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    // 暂未实现密码加密（可以用passwordEncoder）
    // 暂未实现用户名与密码的正则判断
    // 暂时认为注册阶段不需要修改用户信息
    @PostMapping("/register")
    public Map<String, String> register(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return Map.of("message", "密码不一致");
        }
        User user = userService.findByUserName(username);
        if (user == null) {
            userService.register(username, password);
            return Map.of("message", "成功注册");
        } else {
            return Map.of("message", "用户名被占用");
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(String username, String password) {
        User user = userService.findByUserName(username);
        if (user == null) {
            return Map.of("message", "用户名或密码错误");
        }
        if (!user.getPassword().equals(password)) {
            return Map.of("message", "用户名或密码错误");
        } else {
            return Map.of("message", "成功登录");
        }
    }
}
