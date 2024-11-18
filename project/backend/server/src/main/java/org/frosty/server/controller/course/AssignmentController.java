package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.services.course.AssignmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;

    // 教师创建作业
    @PostMapping("/chapter/{id}/assignment")
    public void createAssignment(@PathVariable Long id, @RequestBody Assignment assignment) {
        assignment.setAssignmentId(null);
        Ex.idCheck(id,assignment.getChapterId());
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


    // 获取这一章节的作业列表
    @GetMapping("/chapter/{id}/assignment")
    public Response getAssignmentsByChapterId(@PathVariable Long id) {
        List<Assignment> assignmentList = assignmentService.getAssignmentsByChapterId(id);
        return Response.getSuccess(assignmentList);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentList {
        List<Assignment> content;
    }
}
