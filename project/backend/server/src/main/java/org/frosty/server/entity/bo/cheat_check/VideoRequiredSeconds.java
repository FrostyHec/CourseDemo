package org.frosty.server.entity.bo.cheat_check;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("video_required_seconds")
public class VideoRequiredSeconds {
    @TableId(type = IdType.AUTO)
    private Long videoId;
    private Integer requiredSeconds;
}
