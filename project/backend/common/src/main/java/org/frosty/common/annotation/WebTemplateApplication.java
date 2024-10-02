package org.frosty.common.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportWebTemplate
@SpringBootApplication
@MapperScan
public @interface WebTemplateApplication {

    /**
     * Alias for the {@link MapperScan#basePackages()} attribute.
     * Allows for more concise annotation declarations e.g.:
     * {@code @WebTemplateApplication("com.example.package")} instead of
     * {@code @WebTemplateApplication(basePackages = "com.example.package")}.
     */
    @AliasFor(annotation = MapperScan.class, attribute = "basePackages")
    String[] value() default {};

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces
     * with at least one method will be registered; concrete classes will be
     * ignored.
     */
    @AliasFor(annotation = MapperScan.class, attribute = "basePackages")
    String[] basePackages() default {};
}
