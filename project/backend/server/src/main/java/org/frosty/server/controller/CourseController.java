package org.frosty.server.controller;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.services.CourseService;
import org.frosty.server.entity.po.Course;
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
        course.setId(id);
        course.setStatus(null);
        courseService.updateCourse(id, course);
        return Response.getSuccess("Course updated successfully");
    }


    @GetMapping("/{id}")
    public Response getCourse(@PathVariable Long id) {
        Course course = courseService.getCourse(id);
        return Response.getSuccess(course);
    }
}