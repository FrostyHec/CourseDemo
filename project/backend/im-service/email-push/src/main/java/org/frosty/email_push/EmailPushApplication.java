package org.frosty.email_push;


import org.frosty.common.annotation.WebTemplateApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@WebTemplateApplication(httpLog = true)
public class EmailPushApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailPushApplication.class, args);
    }
}
