package org.frosty.site_dispatch;

import org.frosty.common.annotation.WebTemplateApplication;
import org.springframework.boot.SpringApplication;

@WebTemplateApplication(value = {"org.frosty.site_dispatch.mapper"}, httpLog = true)
public class SiteDispatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SiteDispatchApplication.class, args);
    }
}
