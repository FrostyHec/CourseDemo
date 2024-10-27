package org.frosty.server.services.course;

import lombok.RequiredArgsConstructor;

import org.frosty.server.entity.bo.Course;
import org.frosty.server.mapper.course.CourseMapper;
import org.springframework.dao.DataIntegrityViolationException;
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
        Course course = courseMapper.getCourse(id);
//         检查非法状态转换
        if (course.getStatus().equals(Course.CourseStatus.deleted)) {
            throw new IllegalArgumentException("Invalid status change");
        }
        courseMapper.updateCourseStatus(id, status);
    }

    public void updateCourse(Long id, Course course) {
        // only update name and description, ignore other fields
        courseMapper.updateCourse(id, course.getCourseName(), course.getDescription());
    }

    public Course getCourse(Long id) {
        return courseMapper.getCourse(id);
    }

    public void deleteCourse(Long id) {
        // todo: course status must be "deleted" or course has no student to delete
        courseMapper.deleteCourse(id);
    }

    public List<Course> getAllCourses() {
        return courseMapper.getAllCourses();
    }

    //TODO:模糊搜索
    public List<Course> searchCourses() {
        return List.of();
    }

    public List<Course> findCoursesByTeacherId(Long teacherId) {
        return courseMapper.findCoursesByTeacherId(teacherId);
    }

    public List<Course> adminGetRequiredApprovedCourse(Long adminId) {
        return courseMapper.adminGetRequiredApprovedCourse(adminId);
    }
}