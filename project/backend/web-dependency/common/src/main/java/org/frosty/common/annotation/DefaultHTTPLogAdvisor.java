package org.frosty.common.annotation;

import org.frosty.common.config.HTTPLogAdvisorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HTTPLogAdvisorConfig.class)
public @interface DefaultHTTPLogAdvisor {
    boolean value() default false;
}
