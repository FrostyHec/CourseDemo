package org.frosty.object_storage;

import org.frosty.common.annotation.WebTemplateApplication;
import org.springframework.boot.SpringApplication;


@WebTemplateApplication(httpLog = true)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
