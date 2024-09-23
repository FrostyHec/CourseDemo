package org.frosty.demo.controller;

import java.util.Map;

import org.frosty.demo.config.CommonConstant;
import org.frosty.demo.response.Response;
import org.frosty.demo.services.HelloWorldService;
import org.frosty.demo.verify.Ex;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(CommonConstant.API_VERSION + "/hello")
@RequiredArgsConstructor
public class HelloWorldController {

    private final HelloWorldService helloService;

    @GetMapping
    public Map<String, String> sayHello(@NonNull String say) {
        Ex.check(say.equals("hello"), Response.getBadRequest("invalid-input"));
        return Map.of("say", helloService.getHelloMessage());
    }
}
