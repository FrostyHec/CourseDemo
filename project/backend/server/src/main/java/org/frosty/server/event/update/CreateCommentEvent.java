package org.frosty.server.event.update;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class CreateCommentEvent extends ApplicationEvent {
    private final Long commentId;
    public CreateCommentEvent(Object source, Long commentId) {
        super(source);
        this.commentId = commentId;
    }
}
