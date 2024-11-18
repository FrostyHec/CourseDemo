package org.frosty.server.event.update;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class CompleteCourseEvent extends ApplicationEvent {
    private final Long courseId;
    private final Long studentId;
    public CompleteCourseEvent(Object source, Long courseId, Long studentId) {
        super(source);
        this.courseId = courseId;
        this.studentId = studentId;
    }
}
