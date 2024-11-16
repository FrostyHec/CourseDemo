package org.frosty.server.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationWithReceiver {
    private Long notificationId; //不用填
    private Long courseId;
    private List<Long> receiverIds;
    private String message;
    private String title;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
