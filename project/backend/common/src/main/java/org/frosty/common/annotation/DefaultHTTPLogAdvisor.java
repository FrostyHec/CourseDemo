package org.frosty.common.annotation;

import org.frosty.common.config.HTTPLogAdvisorConfig;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HTTPLogAdvisorConfig.class)
public @interface DefaultHTTPLogAdvisor {
    boolean value() default false;
}
