package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.services.course.CourseService;
import org.frosty.server.entity.bo.Course;
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

    @PutMapping("/{id}/status")
    public Response updateCourseStatus(@PathVariable Long id, @RequestParam String status) {
        courseService.updateCourseStatus(id, status);
        return Response.getSuccess("Course status updated successfully");
    }

    @PutMapping("/{id}")
    public Response updateCourse(@PathVariable Long id, @RequestBody Course course) {
        courseService.updateCourse(id, course);
        return Response.getSuccess("Course updated successfully");
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseService.getCourse(id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseList{
        List<Course> content;
    }
}