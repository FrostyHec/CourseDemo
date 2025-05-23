package org.frosty.object_storage.utils;

import org.frosty.test_common.annotation.ClearOSS;
import org.frosty.test_common.annotation.ControllerTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ClearOSS
@ControllerTest
public @interface ObjectStorageControllerTest {
}
