package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.mapper.course.AssignmentMapper;
import org.frosty.server.services.course.AssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        assignmentMapper.deleteAssById(id);
    }

    @Override
    public Assignment getAssignment(Long assId) {
        return assignmentMapper.selectAssById(assId);
    }

    @Override
    public List<Assignment> getAssignmentsByChapterId(Long id) {
        return assignmentMapper.selectAssignmentsByChapterId(id);
    }
}
