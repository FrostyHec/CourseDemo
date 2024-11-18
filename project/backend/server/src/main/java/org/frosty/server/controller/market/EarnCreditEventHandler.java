package org.frosty.server.controller.market;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.event.delete.UserDeleteEvent;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EarnCreditEventHandler {

    @EventListener
    public void handleCourseCompleteEvent(CompleteCourseEvent event) {
        // TODO 你可以参考这个看看要怎么publishEvent
        // TODO 添加积分，调用MessagePushService发送一条site message(遵照SSE中定义的格式)并记录在history中
    }
    @EventListener
    public void handleCommentCreateEvent(CreateCommentEvent event) {
        // TODO 继续调用service,在service中检查是否为当日的第一条评论，
        //  如果是则添加积分，调用MessagePushService发送一条site message(遵照SSE中定义的格式)并记录在history中
    }


}
