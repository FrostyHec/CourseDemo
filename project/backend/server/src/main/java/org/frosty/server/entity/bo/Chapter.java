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
@TableName("chapters")
public class Chapter {
    @TableId(type = IdType.AUTO)
    private Long chapterId;
    private Long courseId;
    private String chapterTitle;
    private ChapterType chapterType;
    private String content;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public enum ChapterType {
        teaching, assignment, project
    }
}
