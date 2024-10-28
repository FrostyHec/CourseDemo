package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.services.course.AssignmentService;
import org.frosty.server.services.course.CourseService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    // 教师创建作业
    @PostMapping("/chapter/{id}/assignment")
    public void createAssignment(@PathVariable Long id, @RequestBody Assignment assignment) {
        assignmentService.createAssignment(assignment);
    }

    // 教师更新作业
    @PutMapping("/assignment/{id}")
    public void updateAssignment(@PathVariable Long id, @RequestBody Assignment assignment) {
        assignmentService.updateAssignment(assignment);
    }
    // 教师删除作业
    @DeleteMapping("/assignment/{id}")
    public void deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
    }
    // 教师获取单个作业
    @GetMapping("/assignment/{id}")
    public Response getAssignment(@PathVariable Long id) {
        Assignment assignment = assignmentService.getAssignment(id);
        return Response.getSuccess(assignment);
    }
}
