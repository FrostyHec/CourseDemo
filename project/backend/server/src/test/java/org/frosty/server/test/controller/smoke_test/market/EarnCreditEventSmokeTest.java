package org.frosty.server.test.controller.smoke_test.market;

import org.frosty.server.entity.bo.User;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.comment.CommentAPI;
import org.frosty.server.test.controller.market.EarnCreditEventAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class EarnCreditEventSmokeTest {
    @Autowired
    private EarnCreditEventAPI earnCreditEventAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private CommentAPI commentAPI;




    @Test
    public void testEventHandling() throws Exception {
        var name = "test";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token = res.first;
        var uid = res.second;

        var name1 = "test1";
        var res1 = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token1 = res.first;
        var uid1 = res.second;




//        // Simulate course completion event
//        var courseCompleteEvent = new CompleteCourseEvent().setUserId(res.second.getUserId()).setCourseId(1L);
//        earnCreditEventAPI.handleCourseCompleteEvent(token, courseCompleteEvent);
//
//        // Simulate daily comment event
//        var commentEvent = new CreateCommentEvent().setUserId(res.second.getUserId()).setCommentId(1L);
//        earnCreditEventAPI.handleDailyCommentEvent(token, commentEvent);

        // Verify - specific assertions depend on your backend logic
    }
}
