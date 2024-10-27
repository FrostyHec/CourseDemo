package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.services.course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/course/search")
    public Response searchPublicCourse(int page_num, int page_size, String name) {
        if (page_size < -1 || page_num < 0) {
            return Response.getBadRequest("Page parameter error");
        }

        return Response.getSuccess(new CourseList(
                courseService.searchPublicCourse(page_num, page_size, name)));
    }

    @DeleteMapping("/course/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PatchMapping("/course/{id}/publication")
    public void updateCoursePublication(@PathVariable Long id, @RequestBody PublicationStatus publicationStatus) {
        courseService.updateCoursePublication(id, publicationStatus.getPublication().name());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseList {
        private List<Course> content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PublicationStatus {
        private Course.PublicationType publication;
    }

}