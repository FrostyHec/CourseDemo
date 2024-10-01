package org.frosty.server.testutils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.frosty.server.testutils.TestConstant;
import org.frosty.server.config.SpringConfigConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MapperScan({SpringConfigConstant.MAPPER_SCAN_PACKAGE, TestConstant.MAPPER_SCAN_PACKAGE})
@AutoConfigureMockMvc
@TestStateImmutable
public @interface ControllerTest {

}
