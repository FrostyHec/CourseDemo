package org.frosty.server.test.controller.helloworld.testcases;


import org.frosty.server.test.controller.helloworld.HelloWorldAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.frosty.test_common.utils.RespChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class HelloWorldSayTest {
    @Autowired
    private HelloWorldAPI helloWorldAPI;
    @Test
    public void testValidSay() throws Exception {
        helloWorldAPI.say("hello")
                .andExpect(RespChecker.success())
                .andExpect(RespChecker.dataParam("say", "Hello World"));
    }
    @Test
    public void testInvalidSay() throws Exception {
        helloWorldAPI.say("hi")
                .andExpect(RespChecker.badRequest())
                .andExpect(RespChecker.message("invalid-input"));
    }
}