package org.frosty.server.services.course;

import org.frosty.server.entity.bo.Assignment;

public interface AssignmentService {
    void createAssignment(Assignment assignment);

    void updateAssignment(Assignment assignment);

    void deleteAssignment(Long id);

    Assignment getAssignment(Long id);
}
