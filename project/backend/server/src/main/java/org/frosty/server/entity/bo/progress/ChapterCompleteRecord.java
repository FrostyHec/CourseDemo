package org.frosty.server.entity.bo.progress;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("chapter_complete_records")
public class ChapterCompleteRecord {
    private Long chapterId;
    private Long studentId;
}
