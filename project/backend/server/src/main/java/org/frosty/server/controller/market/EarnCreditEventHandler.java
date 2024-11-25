package org.frosty.server.controller.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.server.event.delete.UserDeleteEvent;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;
import org.frosty.server.services.market.EarnCreditEventService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EarnCreditEventHandler {
    private final EarnCreditEventService earnCreditEventService;

    @EventListener
    public void handleCourseCompleteEvent(CompleteCourseEvent event) {
        // TODO 你可以参考这个看看要怎么publishEvent
        // TODO 添加积分，调用MessagePushService发送一条site message(遵照SSE中定义的格式)并记录在history中
        earnCreditEventService.handleCourseComplete(event);

    }

    @EventListener
    public void handleCommentCreateEvent(CreateCommentEvent event) {
        // TODO 继续调用service,在service中检查是否为当日的第一条评论，
        //  如果是则添加积分，调用MessagePushService发送一条site message(遵照SSE中定义的格式)并记录在history中
        earnCreditEventService.handleDailyComment(event);
    }


    @AllArgsConstructor
    @Getter
    public enum ScoreRule {
        COMPLETE_COURSE(200),
        DAILY_COMMENT(10);
        private final int score; // 积分值
    }

}
