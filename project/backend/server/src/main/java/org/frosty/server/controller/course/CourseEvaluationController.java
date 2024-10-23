package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API+"/course/{id}")
@RequiredArgsConstructor
public class CourseEvaluationController {

    @PostMapping("/evaluation")
    public void createEvaluation(@PathVariable String id, @RequestBody CourseEvaluation courseEvaluation) {
        FrameworkUtils.notImplemented();
    }

    @PutMapping("/evaluation")
    public void updateEvaluation(@PathVariable String id,@RequestBody CourseEvaluation courseEvaluation) {
        FrameworkUtils.notImplemented();
    }

    @DeleteMapping("/evaluation")
    public void deleteEvaluation(@PathVariable String id) {
        FrameworkUtils.notImplemented();
    }

    @GetMapping("/evaluation")
    public CourseEvaluation getEvaluation(@PathVariable String id) {
        FrameworkUtils.notImplemented();
        return null;
    }

    @GetMapping("/evaluations")
    public CourseEvaluationList getEvaluations(@PathVariable String id, int page_size,int page_num) {
        FrameworkUtils.notImplemented();
        return null;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseEvaluationList{
        private List<CourseEvaluation> content;
    }

}
