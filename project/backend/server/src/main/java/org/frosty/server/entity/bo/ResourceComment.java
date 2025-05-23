package org.frosty.server.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("resource_comments")
public class ResourceComment {
    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Long resourceId;
    private Long userId;
    private String commentText;
    private Long commentReply;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}