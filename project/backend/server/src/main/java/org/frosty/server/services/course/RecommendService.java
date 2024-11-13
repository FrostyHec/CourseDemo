package org.frosty.server.services.course;


import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.course.RecommendController;
import org.frosty.server.mapper.course.CourseMapper;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    public List<RecommendController.CourseWithStudentCount> getHotCourses(int page_size, int page_num) {
        return courseMapper.getHotCourses(page_num - 1, page_size); // sql里面的pageNum是从0开始的
    }

    public List<RecommendController.TeacherWithStudentCount> getHotTeachers(int page_size, int page_num) {
        return userMapper.getHotTeachers(page_num - 1, page_size);
    }
}
