package org.frosty.server.entity.bo.market.action_type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteCourseActionParam implements ActionParam{
    private Long courseId;
    private String courseName;
}
