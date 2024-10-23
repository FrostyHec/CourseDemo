package org.frosty.server.services.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.CourseLike;
import org.frosty.server.mapper.course.CourseLikeMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseLikeService {
    private final CourseLikeMapper courseLikeMapper;

    public void createCourseLike(Long courseId, Long userId) {
        courseLikeMapper.insert(new CourseLike(courseId, userId));
    }


    public void deleteCourseLike(Long courseId, Long userId) {
        QueryWrapper<CourseLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .eq("student_id", userId);
        courseLikeMapper.delete(queryWrapper);
    }


    public boolean checkCourseLike(Long courseId, Long userId) {
        QueryWrapper<CourseLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .eq("student_id", userId);  // 通过多个主键字段设置查询条件

        return courseLikeMapper.selectOne(queryWrapper) == null;
    }


}
