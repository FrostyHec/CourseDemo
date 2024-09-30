package org.forsty.auth_filter;

import org.forsty.auth_filter.config.CommonConstant;
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
