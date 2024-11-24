package org.frosty.server.entity.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("notification_receiver")
public class NotificationReceiver {
    private Long notificationId;
    private Long receiverId;
}