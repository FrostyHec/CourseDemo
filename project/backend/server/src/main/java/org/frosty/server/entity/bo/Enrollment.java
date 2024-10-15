package org.frosty.server.entity.bo;

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
@TableName("enrollments")
public class Enrollment {
    private int studentId;
    private int courseId;
    private EnrollmentType status;
    private OffsetDateTime createAt;
    private OffsetDateTime updatedAt;
    public enum EnrollmentType {
       publik,invited
    }
}