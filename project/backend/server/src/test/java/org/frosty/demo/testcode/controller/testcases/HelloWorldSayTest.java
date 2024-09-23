package org.frosty.demo.testcode.controller.testcases;

import org.frosty.demo.testcode.controller.HelloWorldAPI;
import org.frosty.demo.testutils.RespChecker;
import org.frosty.demo.testutils.annotation.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ControllerTest
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