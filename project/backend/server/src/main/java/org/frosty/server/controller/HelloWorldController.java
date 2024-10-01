package org.frosty.server.controller;

import java.util.Map;

import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.services.HelloWorldService;
import org.frosty.common.verify.Ex;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PathConstant.API + "/hello")
@RequiredArgsConstructor
public class HelloWorldController {

    private final HelloWorldService helloService;

    @GetMapping
    public Map<String, String> sayHello(@NonNull String say) {
        Ex.check(say.equals("hello"), Response.getBadRequest("invalid-input"));
        return Map.of("say", helloService.getHelloMessage());
    }
}
