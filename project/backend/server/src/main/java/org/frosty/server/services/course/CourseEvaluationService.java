package org.frosty.server.services.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.CourseEvaluation;
import org.frosty.server.mapper.course.CourseEvaluationMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseEvaluationService {
    private final CourseEvaluationMapper courseEvaluationMapper;

    public void createEvaluation(CourseEvaluation courseEvaluation) {
        courseEvaluationMapper.insert(courseEvaluation);
    }

    public void updateEvaluation(CourseEvaluation courseEvaluation) {
        courseEvaluationMapper.updateById(courseEvaluation);
    }

    public void deleteEvaluation(Long courseId, Long studentId) {
        QueryWrapper<CourseEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .eq("student_id", studentId);
        courseEvaluationMapper.delete(queryWrapper);
    }

    public CourseEvaluation getEvaluationByCourseAndStudent(Long courseId, Long studentId) {
        QueryWrapper<CourseEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .eq("student_id", studentId);
        return courseEvaluationMapper.selectOne(queryWrapper);
    }

    public List<CourseEvaluation> getAllEvaluationsByCourse(Long courseId, int page_size, int page_num) {
        QueryWrapper<CourseEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        if (page_size == -1) {
            return courseEvaluationMapper.selectList(queryWrapper);
        }
        Page<CourseEvaluation> page = new Page<>(page_num, page_size);
        return courseEvaluationMapper.selectPage(page, queryWrapper).getRecords();
    }
}
