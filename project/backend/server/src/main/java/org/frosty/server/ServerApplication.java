package org.frosty.server;

import org.frosty.server.config.SpringConfigConstant;
import org.frosty.common.annotation.WebTemplateApplication;
import org.springframework.boot.SpringApplication;

@WebTemplateApplication({SpringConfigConstant.MAPPER_SCAN_PACKAGE})
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
