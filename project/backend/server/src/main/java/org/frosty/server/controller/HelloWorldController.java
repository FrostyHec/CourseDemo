package org.frosty.server.controller;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.infra.initalizer.DatabaseInitializer;
import org.frosty.server.services.HelloWorldService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API + "/hello")
@RequiredArgsConstructor
public class HelloWorldController {
    private final DatabaseInitializer databaseInitializer;
    private final HelloWorldService helloService;

    @GetMapping
    public Map<String, String> sayHello(String say) throws IOException {
        databaseInitializer.init();
        Ex.check("hello".equals(say), Response.getBadRequest("invalid-input"));
        return Map.of("say", helloService.getHelloMessage());
    }
}
