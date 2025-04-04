package org.frosty.server.entity.bo.cheat_check;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("video_watch_records")
public class VideoWatchRecord {
    private Long videoId;
    private Long studentId;
    private Integer remainRequiredSeconds;
    private Integer lastWatchedSeconds;
}
