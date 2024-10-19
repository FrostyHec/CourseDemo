package org.frosty.email_push.testcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common_service.im.entity.Email;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@RequiredArgsConstructor
public class EmailPushAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Value("${spring.mail.test.from-address}")
    private String testFromEmail;

    @Value("${spring.mail.test.from-name}")
    private String testFromName;

    @Value("${spring.mail.test.to-address}")
    private String testToEmail;

    public Email getTemplateEmail(){
        return getTemplateEmail("OOAD Test","Testing");
    }
    public Email getTemplateEmail(String subject,String text){
        return new Email()
                .setFromEmail(testFromEmail)
                .setFromName(testFromName)
                .setToEmail(testToEmail)
                .setSubject(subject)
                .setText(text)
                ;
    }

    public ResultActions send(Email email) throws Exception {
        String baseUrl = PathConstant.INTERNAL_API + "/msg/email";
        String json = objectMapper.writeValueAsString(email);
        return mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void sendSuccess(Email email) throws Exception {
        send(email)
                .andExpect(RespChecker.success());
    }
}
