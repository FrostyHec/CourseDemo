package org.frosty.sse_push.annotation;
//TODO  find ways for fixing the annotation failure for basedMapper api

import org.frosty.sse_push.handler.DynamicTableNameType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicTableNameMapper {
    DynamicTableNameType type() default DynamicTableNameType.NO;

    String name() default "";
}
