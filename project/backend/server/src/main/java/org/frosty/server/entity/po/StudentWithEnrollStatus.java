package org.frosty.server.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frosty.server.entity.bo.Enrollment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentWithEnrollStatus {
    private UserPublicInfo student;
    private Enrollment.EnrollmentType status;
}