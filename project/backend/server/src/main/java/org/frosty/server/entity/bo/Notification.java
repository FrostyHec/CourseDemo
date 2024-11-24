package org.frosty.server.entity.bo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("notifications")
public class Notification {
    @TableId
    private Long notificationId;
    private Long courseId;
    //    private List<Integer> receiverIds;
    private String title;
    private String message;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
