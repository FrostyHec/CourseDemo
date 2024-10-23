package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.services.course.EvaluationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    /**
     * 发布课程评价
     *
     * @param courseId         课程ID
     * @param courseEvaluation 评价内容
     */
    @PostMapping("/course/{id}/evaluation")
    public void postEvaluation(@PathVariable Long id, @RequestBody CourseEvaluation courseEvaluation) {
        courseEvaluation.setCourseId(id);
        evaluationService.createEvaluation(courseEvaluation);
    }

    /**
     * 更新课程评价
     *
     * @param id         课程ID
     * @param courseEvaluation 更新的评价内容
     */
    @PutMapping("/course/{id}/evaluation")
    public void updateEvaluation(@PathVariable Long id, @RequestBody CourseEvaluation courseEvaluation) {
        courseEvaluation.setCourseId(id);
        evaluationService.updateEvaluation(courseEvaluation);
    }

    /**
     * 删除课程评价
     *
     * @param id  课程ID
     * @param studentId 学生ID
     */
    @DeleteMapping("/course/{id}/evaluation")
    public void deleteEvaluation(@PathVariable Long id, @RequestParam Long studentId) {
        evaluationService.deleteEvaluation(id, studentId);
    }

    /**
     * 查看我的评价
     *
     * @param id  课程ID
     * @param studentId 学生ID
     * @return 返回我的评价
     */
    @GetMapping("/course/{id}/evaluation")
    public CourseEvaluation getMyEvaluation(@PathVariable Long id, @RequestParam Long studentId) {
        return evaluationService.getEvaluationByCourseAndStudent(id, studentId);
    }

    /**
     * 查看全部评价
     *
     * @param id 课程ID
     * @return 返回课程的所有评价
     */
    @GetMapping("/course/{id}/evaluations")
    public Map<String, Object> getAllEvaluations(@PathVariable Long id) {
        throw new NotImplementedException();
//        List<Map<String, Object>> evaluations = evaluationService.getAllEvaluationsByCourse(courseId);
//        return Map.of("content", evaluations);
    }
}
