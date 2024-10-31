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
@TableName("file_submission")
public class FileSubmission {
    @TableId(type = IdType.AUTO)
    private Long fileSubmissionId;
    private Long assignmentId;
    private Long studentId;
    private String fileName;
    private Integer gainedScore;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
