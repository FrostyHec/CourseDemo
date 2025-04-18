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
@TableName("msg_unposed")
public class UnposedSiteMessage extends SiteMessage {
    @TableId(type = IdType.ASSIGN_ID)
    protected Long messageId;
}
