package org.frosty.sse_push;

import org.frosty.common.annotation.WebTemplateApplication;
import org.springframework.boot.SpringApplication;


@WebTemplateApplication(value = {"org.frosty.sse_push.mapper"}, httpLog = true)
public class SitePushApplication {
    public static void main(String[] args) {
        SpringApplication.run(SitePushApplication.class, args);
    }
}
