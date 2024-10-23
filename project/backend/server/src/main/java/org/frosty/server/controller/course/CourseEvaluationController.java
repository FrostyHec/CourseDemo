package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.services.course.CourseEvaluationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API + "/course/{id}")
@RequiredArgsConstructor
public class CourseEvaluationController {

    private final CourseEvaluationService courseEvaluationService;

    /**
     * 发布课程评价
     *
     * @param id               课程ID
     * @param courseEvaluation 评价内容
     */
    @PostMapping("/evaluation")
    public void createEvaluation(@PathVariable Long id, @GetToken TokenInfo tokenInfo, @RequestBody CourseEvaluation courseEvaluation) {
        courseEvaluation.setCourseId(id)
                .setStudentId(tokenInfo.getAuthInfo().getUserID());
        courseEvaluationService.createEvaluation(courseEvaluation);
    }

    /**
     * 更新课程评价
     *
     * @param id               课程ID
     * @param courseEvaluation 更新的评价内容
     */
    @PutMapping("/evaluation")
    public void updateEvaluation(@PathVariable Long id, @GetToken TokenInfo tokenInfo, @RequestBody CourseEvaluation courseEvaluation) {
        courseEvaluation.setCourseId(id)
                .setStudentId(tokenInfo.getAuthInfo().getUserID());
        courseEvaluationService.updateEvaluation(courseEvaluation);
    }

    /**
     * 删除课程评价
     *
     * @param id        课程ID
     * @param tokenInfo 学生登录信息
     */
    @DeleteMapping("/evaluation")
    public void deleteEvaluation(@PathVariable Long id, @GetToken TokenInfo tokenInfo) {//, @RequestParam Long studentId
        courseEvaluationService.deleteEvaluation(id, tokenInfo.getAuthInfo().getUserID());
    }

    /**
     * 查看我的评价
     *
     * @param id        课程ID
     * @param tokenInfo 学生登录信息
     * @return 返回我的评价
     */
    @GetMapping("/evaluation")
    public CourseEvaluation getEvaluation(@PathVariable Long id, @GetToken TokenInfo tokenInfo) {
        return courseEvaluationService.getEvaluationByCourseAndStudent(id, tokenInfo.getAuthInfo().getUserID());
    }

    /**
     * 查看全部评价
     *
     * @param id 课程ID
     * @return 返回课程的所有评价
     */
    @GetMapping("/evaluations")
    public CourseEvaluationList getEvaluations(@PathVariable Long id, int page_size, int page_num) {
        if (page_size < -1 || page_num < 0) {
            return null; // bad request
        }
        return new CourseEvaluationList(courseEvaluationService.getAllEvaluationsByCourse(id, page_size, page_num));

//        List<Map<String, Object>> evaluations = evaluationService.getAllEvaluationsByCourse(courseId);
//        return Map.of("content", evaluations);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseEvaluationList {
        private List<CourseEvaluation> content;
    }


}
