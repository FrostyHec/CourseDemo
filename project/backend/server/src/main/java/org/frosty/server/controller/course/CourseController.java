package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.services.course.CourseService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(PathConstant.API + "/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public Response createCourse(@RequestBody Course course) {
        courseService.createCourse(course);
        return Response.getSuccess("Course created successfully");
    }

    @PatchMapping("/{id}/status")
    public Response updateCourseStatus(@PathVariable Long id, @RequestBody String status) {
        courseService.updateCourseStatus(id, status);
        return Response.getSuccess("Course status updated successfully");
    }

    @PutMapping("/{id}")
    public Response updateCourse(@PathVariable Long id, @RequestBody Course course) {
        // 确保忽略前端传递的敏感字段，如 id 和 status
        course.setCourseId(id);
        course.setStatus(null);
        courseService.updateCourse(id, course);
        return Response.getSuccess("Course updated successfully");
    }


    @GetMapping("/{id}")
    public Response getCourse(@PathVariable Long id) {
        Course course = courseService.getCourse(id);
        return Response.getSuccess(course);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseList{
        List<Course> content;
    }
}