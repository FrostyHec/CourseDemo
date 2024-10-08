package org.frosty.server.services.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final ApplicationEventPublisher eventPublisher;

    // TODO example for event mechanism to handle delete
    public void deleteUserById(Long userId) {
        publishDeleteUser(userId);
    }

    public void publishDeleteUser(Long userId){
        eventPublisher.publishEvent(new UserDeleteEvent(this, userId));
    }

    @EventListener
    public void handleUserDeleteEvent(UserDeleteEvent event) {
        log.info("user_deleted");
    }

    @Getter
    public static class UserDeleteEvent extends ApplicationEvent {
        private final Long userId;

        public UserDeleteEvent(Object source, Long userId) {
            super(source);
            this.userId = userId;
        }

    }
}
