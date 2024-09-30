package org.frosty.server.testcode.controller.testcases;

import org.frosty.server.testcode.controller.HelloWorldAPI;
import org.frosty.server.testutils.RespChecker;
import org.frosty.server.testutils.annotation.ControllerTest;
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