package org.frosty.server;


import org.frosty.common.annotation.WebTemplateApplication;
import org.frosty.server.config.SpringConfigConstant;
import org.springframework.boot.SpringApplication;

@WebTemplateApplication(value = {SpringConfigConstant.MAPPER_SCAN_PACKAGE}, httpLog = true)
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
