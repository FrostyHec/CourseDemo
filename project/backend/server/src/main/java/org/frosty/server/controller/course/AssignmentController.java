package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class AssignmentController {
    // TODO QZH
    @PostMapping("/chapter/{id}/assignment")
    public void createAssignment(@PathVariable String id, @RequestBody Assignment assignment) {
        FrameworkUtils.notImplemented();
    }
    @PutMapping("/assignment/{id}")
    public void updateAssignment(@PathVariable String id, @RequestBody Assignment assignment) {
        FrameworkUtils.notImplemented();
    }
    // delete
    @DeleteMapping("/assignment/{id}")
    public void deleteAssignment(@PathVariable String id) {
        FrameworkUtils.notImplemented();
    }
    // get
    @GetMapping("/assignment/{id}")
    public Assignment getAssignment(@PathVariable String id) {
        FrameworkUtils.notImplemented();
        return null;
    }
}
