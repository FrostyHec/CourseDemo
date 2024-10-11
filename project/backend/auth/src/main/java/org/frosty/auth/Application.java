package org.frosty.auth;

import org.frosty.auth.config.SpringConfigConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(SpringConfigConstant.MAPPER_SCAN_PACKAGE)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
