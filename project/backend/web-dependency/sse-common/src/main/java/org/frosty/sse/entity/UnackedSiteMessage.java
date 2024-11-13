package org.frosty.sse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("msg_unacked")
public class UnackedSiteMessage extends SiteMessage {
    @TableId(type = IdType.ASSIGN_ID)
    protected Long messageId;
}
