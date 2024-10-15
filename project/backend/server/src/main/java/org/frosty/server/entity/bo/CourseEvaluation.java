package org.frosty.server.entity.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("course_evaluations")
public class CourseEvaluation {
    private Long courseId;
    private Long studentId;
    private String comment;
    private Integer score;
    private JsonNode evaluationFormAnswer;
    private OffsetDateTime createAt;
    private OffsetDateTime updatedAt;
}