package org.frosty.server.services.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.mapper.course.EvaluationMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationMapper evaluationMapper;

    public void createEvaluation(CourseEvaluation courseEvaluation) {
        evaluationMapper.insert(courseEvaluation);
    }

    public void updateEvaluation(CourseEvaluation courseEvaluation) {
        evaluationMapper.updateById(courseEvaluation);
    }

    public void deleteEvaluation(Long courseId, Long studentId) {
        QueryWrapper<CourseEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .eq("student_id", studentId);
        evaluationMapper.delete(queryWrapper);
    }

    public CourseEvaluation getEvaluationByCourseAndStudent(Long courseId, Long studentId) {
        QueryWrapper<CourseEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .eq("student_id", studentId);
        return evaluationMapper.selectOne(queryWrapper);
    }

    public List<CourseEvaluation> getAllEvaluationsByCourse(Long courseId) {
        QueryWrapper<CourseEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        return evaluationMapper.selectList(queryWrapper);
    }
}
