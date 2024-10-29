package org.frosty.server.test.controller.smoke_test;

import org.frosty.test_common.annotation.IdempotentControllerTest;

@IdempotentControllerTest
public class CourseEvaluationSmokeTest {
    // TODO 基础测试
    // 1. 学生对某个课程发送课程评价
    // 2. 更新
    // 3. 删除
    // 4. 获取
    // 需要注意:模拟测试时， private JsonNode evaluationFormAnswer; 这个应该可以支持传入并存储任意样式的json（后续会依据表单type检查）

}
