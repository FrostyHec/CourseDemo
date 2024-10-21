package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.po.StudentWithEnrollStatus;
import org.frosty.server.services.course.CourseService;
import org.frosty.server.services.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseMemberController {
    private final CourseService courseService;
    private final UserService userService;

    // 学生申请加入课程
    @PostMapping("/course/{id}/student/enroll")
    public Response enrollStudentsToCourse(@PathVariable Long id) {
        //todo
        return Response.getSuccess("Students enrolled successfully");
    }

    // 教师添加学生入课程
    @PostMapping("/course/{id}/teacher/invite")
    public Response inviteStudentsToCourse(@PathVariable Long id, @RequestBody StudentList studentList) {
        throw new NotImplementedException();
//        courseService.inviteStudents(id, Arrays.stream(studentList).boxed().collect(Collectors.toList()));
//        return Response.getSuccess("Students invited successfully");
    }

    // 获取课程中的学生列表
    @GetMapping("/course/{id}/student")
    public Response getStudentList(@PathVariable Long id,
                                   @RequestParam int page_num,
                                   @RequestParam int page_size) {
        throw new NotImplementedException();
//        List<Map<String, Object>> studentList = userService.getStudentListByCourse(id, page_num, page_size);
//        return Response.getSuccess(studentList);
    }

    // 获取学生加入的课程列表
    @GetMapping("/student/{id}/courses")
    public Response getStudentCourses(@PathVariable Long id,
                                      @RequestParam int page_num,
                                      @RequestParam int page_size) {
        throw new NotImplementedException();
//        List<Course> courses = courseService.getCoursesByStudentId(id, page_num, page_size);
//        return Response.getSuccess(new CourseList(courses));
    }

    // 获取教师教授的课程列表
    @GetMapping("/teacher/{id}/courses")
    public Response getTeacherCourses(@PathVariable Long id,
                                      @RequestParam int page_num,
                                      @RequestParam int page_size) {
        throw new NotImplementedException();
//        List<Course> courses = courseService.getCoursesByTeacherId(id, page_num, page_size);
//        return Response.getSuccess(new CourseList(courses));
    }

    // 获取某个学生的入课状态
    @GetMapping("/course/{id}/student/{studentId}/status")
    public Response getStudentStatus(@PathVariable Long id,
                                     @PathVariable Long studentId) {
        throw new NotImplementedException();
//        Map<String, Object> studentStatus = userService.getPendingStudentStatus(id, studentId, page_num, page_size);
//        return Response.getSuccess(studentStatus);
    }

    // 管理员获取待审核的课程列表
    @GetMapping("/admin/{id}/courses/submitted")
    public Response getSubmittedCourses(@PathVariable Long id,
                                        @RequestParam int page_num,
                                        @RequestParam int page_size) {
        throw new NotImplementedException();
//        List<Course> submittedCourses = courseService.getSubmittedCourses(id, page_num, page_size);
//        return Response.getSuccess(new CourseList(submittedCourses));
    }

    // 内部类用于返回课程列表
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseList {
        private List<Course> content;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentStatusList {
        private List<StudentWithEnrollStatus> studentList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentList{
        private List<Long> studentList;
    }
}
