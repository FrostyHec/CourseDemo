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
@TableName("courses")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long courseId;
    private Long teacherId;
    private String courseName;
    private String description;
    private CourseStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private PublicationType publication;
    private CourseEvaluationType evaluationType;

    public enum CourseEvaluationType{
        practice,theory
    }

    public enum CourseStatus {
        creating,
        submitted,
        published,
        rejected,
        archived,
        deleted
    }

    public enum PublicationType {
        open, closed, semi_open
    }
}