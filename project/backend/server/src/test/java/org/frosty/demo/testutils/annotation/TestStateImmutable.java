package org.frosty.demo.testutils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.frosty.demo.testutils.EnsureTestStateImmutabilityFilter;
import org.junit.jupiter.api.extension.ExtendWith;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(EnsureTestStateImmutabilityFilter.class)
public @interface TestStateImmutable {
}
