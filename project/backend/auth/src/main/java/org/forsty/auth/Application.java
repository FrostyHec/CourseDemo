package org.forsty.auth;

import org.forsty.auth.config.CommonConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(CommonConstant.MAPPER_SCAN_PACKAGE)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
