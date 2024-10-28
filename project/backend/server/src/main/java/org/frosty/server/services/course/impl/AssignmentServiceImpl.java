package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.mapper.course.AssignmentMapper;
import org.frosty.server.services.course.AssignmentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentMapper assignmentMapper;

    @Override
    public void createAssignment(Assignment assignment) {
        assignmentMapper.insert(assignment);
    }

    @Override
    public void updateAssignment(Assignment assignment) {
        assignmentMapper.updateById(assignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        assignmentMapper.deleteById(id);
    }

    @Override
    public Assignment getAssignment(Long assId) {
        return assignmentMapper.selectAssById(assId);
    }
}
