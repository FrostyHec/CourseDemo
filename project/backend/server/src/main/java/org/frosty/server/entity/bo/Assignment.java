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
@TableName("assignments")
public class Assignment implements ChapterContent {
    @TableId(type = IdType.AUTO)
    private Long assignmentId;
    private Long chapterId;
    private String assignmentName;
    private String description;
    private AssignmentType assignmentType;
    private Boolean allowUpdateSubmission;
    private OffsetDateTime latestSubmissionTime;
    private Integer maximumScore;
    private Boolean allowStudentToViewScore;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public enum AssignmentType {
        file_upload, online_form
    }
}
