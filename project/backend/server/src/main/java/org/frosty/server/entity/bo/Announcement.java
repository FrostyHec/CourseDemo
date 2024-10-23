package org.frosty.server.entity.bo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("notifications")
public class Announcement {
    @TableId
    private Long notificationId;
    private Long courseId;
    private List<Integer> receiverIds;
    private String message;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
