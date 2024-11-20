package org.frosty.server.test.controller.smoke_test.course.progress;

import org.frosty.test_common.annotation.IdempotentControllerTest;

@IdempotentControllerTest
public class CourseProgressSmokeTest {
    // 测试1resource1chapter1course的情况下直接调用complete resource/chapter/course会失败（高级测试）
    // 测试能够正常的complete resource，chapter和course

}
