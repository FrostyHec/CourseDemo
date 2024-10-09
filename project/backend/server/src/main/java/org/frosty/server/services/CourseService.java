package org.frosty.server.services;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.po.Course;
import org.frosty.server.mapper.CourseMapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;

    public void createCourse(Course course) {
        courseMapper.insertCourse(course);
    }

    public void updateCourseStatus(Long id, String status) {
        courseMapper.updateCourseStatus(id, status);
    }

    public void updateCourse(Long id, Course course) {
        courseMapper.updateCourse(id, course.getName(), course.getDescription());
    }

    public Course getCourse(Long id) {
        return courseMapper.getCourse(id);
    }

    public void deleteCourse(Long id) {
        courseMapper.deleteCourse(id);
    }

    public List<Course> getAllCourses() {
        return courseMapper.getAllCourses();
    }
}