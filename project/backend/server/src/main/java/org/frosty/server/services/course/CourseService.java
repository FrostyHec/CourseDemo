package org.frosty.server.services.course;

import lombok.RequiredArgsConstructor;

import org.frosty.server.entity.bo.Course;
import org.frosty.server.mapper.course.CourseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;

    public void createCourse(Course course) {
//        courseMapper.insertCourse(course.getCourseName(),course.getDescription(), CourseStatus.CREATING.name());
    }

    public void updateCourseStatus(Long id, String status) {
        Course course = courseMapper.getCourse(id);
        // 检查非法状态转换
//        if (course.getStatus().equals(CourseStatus.DELETED) ||
//                !status.equals(CourseStatus.CREATING.name())) {
//            throw new IllegalArgumentException("Invalid status change");
//        }
        courseMapper.updateCourseStatus(id, status);
    }

    public void updateCourse(Long id, Course course) {
        // 只更新名称和描述，忽略其他字段
//        courseMapper.updateCourse(id, course.getName(), course.getDescription());
    }

    public Course getCourse(Long id) {
        return courseMapper.getCourse(id);
    }

    public void deleteCourse(Long id) {
        //TODO:课程状态必须为“已删除”或者没有学生的课程才能删除
        courseMapper.deleteCourse(id);
    }

    public List<Course> getAllCourses() {
        return courseMapper.getAllCourses();
    }

    //TODO:模糊搜索
    public List<Course> searchCourses() {
        return List.of();
    }
}