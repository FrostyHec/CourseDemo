package org.frosty.email_push.testcode.controller.testcases;


import org.frosty.email_push.testcode.controller.EmailPushAPI;
import org.frosty.test_common.annotation.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ControllerTest
public class EmailPushTest {
    @Autowired
    private EmailPushAPI emailPushAPI;
    @Test
    public void checkCorrectlySend() throws Exception {
        var e = emailPushAPI.getTemplateEmail();
        emailPushAPI.sendSuccess(e);
    }
}
