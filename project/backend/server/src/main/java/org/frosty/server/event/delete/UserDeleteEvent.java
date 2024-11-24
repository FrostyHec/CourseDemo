package org.frosty.server.event.delete;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserDeleteEvent extends ApplicationEvent {
    private final Long userId;

    public UserDeleteEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

}
