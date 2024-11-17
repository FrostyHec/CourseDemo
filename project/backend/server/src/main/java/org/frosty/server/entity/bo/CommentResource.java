package org.frosty.server.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment_resources")
public class CommentResource {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long commentId;
    private String resourceName;
    private String fileName;
    private String suffix;
}
