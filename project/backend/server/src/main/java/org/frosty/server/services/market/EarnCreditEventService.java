package org.frosty.server.services.market;

import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;

public interface EarnCreditEventService {
    void handleCourseComplete(CompleteCourseEvent event);

    void handleDailyComment(CreateCommentEvent event);
}
