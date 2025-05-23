package org.frosty.test_common.annotation;

import org.frosty.test_common.handler.DatabaseIdempotencyHandler;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DatabaseIdempotencyHandler.class)
public @interface DatabaseIdempotency {
}
