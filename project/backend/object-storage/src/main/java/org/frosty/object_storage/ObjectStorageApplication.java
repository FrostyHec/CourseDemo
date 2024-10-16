package org.frosty.object_storage;

import org.frosty.common.annotation.WebTemplateApplication;
import org.springframework.boot.SpringApplication;


@WebTemplateApplication(value = {"org.frosty.object_storage.mapper"},httpLog = true)
public class ObjectStorageApplication {
	public static void main(String[] args) {
		SpringApplication.run(ObjectStorageApplication.class, args);
	}
}
