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
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/course")
    public Response createCourse(@RequestBody Course course) {
        courseService.createCourse(course);
        return Response.getSuccess("Course created successfully");
    }

    @PatchMapping("course/{id}/status")
    public Response updateCourseStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        String status = statusMap.get("status");
        courseService.updateCourseStatus(id, status);
        return Response.getSuccess("Course status updated successfully");
    }

    @PutMapping("course/{id}")
    public Response updateCourse(@PathVariable Long id, @RequestBody Course course) {
        // 确保忽略前端传递的敏感字段，如 id 和 status
        course.setCourseId(id);
        course.setStatus(null);
        courseService.updateCourse(id, course);
        return Response.getSuccess("Course updated successfully");
    }


    @GetMapping("course/{id}")
    public Response getCourse(@PathVariable Long id) {
        Course course = courseService.getCourse(id);
        return Response.getSuccess(course);
    }

    @GetMapping("teacher/{id}/courses")
    public Response findCoursesByTeacherId(@PathVariable Long id) {
        List<Course> courses = courseService.findCoursesByTeacherId(id);
        return Response.getSuccess(new CourseList(courses));
    }
    @GetMapping("/admin/{id}/courses/submitted")
   public Response adminGetRequiredApprovedCourse (@PathVariable Long id){
        List<Course> courses = courseService.adminGetRequiredApprovedCourse(id);
        return Response.getSuccess(new CourseList(courses));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseList{
        List<Course> content;
    }
}