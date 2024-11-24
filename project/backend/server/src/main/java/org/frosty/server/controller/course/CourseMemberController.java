package org.frosty.server.controller.course;

import lombok.*;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.Enrollment;
import org.frosty.server.entity.po.StudentWithEnrollStatus;
import org.frosty.server.services.course.CourseMemberService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseMemberController {
    private final CourseMemberService courseMemberService;

    // 学生申请加入课程
    @PostMapping("/course/{id}/student/enroll")
    public Response enrollStudentsToCourse(@PathVariable Long id,
                                           @GetToken TokenInfo tokenInfo) {
        Long userId = tokenInfo.getAuthInfo().getUserID();
        courseMemberService.enrollStudentsToCourse(id, userId);
        return Response.getSuccess("Students enrolled successfully");
    }

    // 教师添加学生入课程
    @PostMapping("/course/{id}/teacher/invite")
    public Response inviteStudentsToCourse(@PathVariable Long id, @RequestBody StudentList studentList) {
        courseMemberService.inviteStudentsToCourse(id, studentList.getStudentList());
        return Response.getSuccess("Invite students successfully");
    }

    // 获取课程中的学生列表
    @GetMapping("/course/{id}/student")
    public StudentStatusList getStudentList(@PathVariable Long id,
                                   @RequestParam int page_num,
                                   @RequestParam int page_size) {
        if (page_size < -1 || page_num < 0) {
            throw new ExternalException(Response.getBadRequest("Page parameter error"));
        }

        return new StudentStatusList(
                courseMemberService.getStudentList(id, page_num, page_size));
    }

    // 获取学生加入的课程列表
    @GetMapping("/student/{id}/courses")
    public Response getStudentCourses(@PathVariable Long id,
                                      @RequestParam int page_num,
                                      @RequestParam int page_size) {
        if (page_size < -1 || page_num < 0) {
            return Response.getBadRequest("Page parameter error");
        }

        return Response.getSuccess(new CourseList(
                courseMemberService.getStudentCourses(id, page_num, page_size)));
    }

    // 获取教师教授的课程列表
    @GetMapping("/teacher/{id}/courses")
    public CourseList getTeacherCourses(@PathVariable Long id,
                                      @RequestParam int page_num,
                                      @RequestParam int page_size) {
        if (page_size < -1 || page_num < 0) {
            throw new ExternalException(Response.getBadRequest("Page parameter error"));//TODO
        }
        var li = courseMemberService.getTeacherCourses(id, page_num, page_size);
        return new CourseList(li);
    }

    // 获取某个学生的入课状态
    @GetMapping("/course/{id}/student/{studentId}/status")
    public Response getStudentStatus(@PathVariable Long id,
                                     @PathVariable Long studentId) {
        return Response.getSuccess(courseMemberService.getStudentStatus(id, studentId));
    }

    // 管理员获取待审核的课程列表
    @GetMapping("/admin/{id}/courses/submitted")
    public Response getSubmittedCourses(@PathVariable Long id,
                                        @RequestParam int page_num,
                                        @RequestParam int page_size) {
        if (page_size < -1 || page_num < 0) {
            return Response.getBadRequest("Page parameter error");
        }

        return Response.getSuccess(new CourseList(
                courseMemberService.getSubmittedCourses(page_num, page_size)));
    }
    @PutMapping("/course/{id}/student/{studentId}/status")
    public void updateStudentEnrollStatus(@PathVariable String id,
                                          @PathVariable String studentId,
                                          @RequestBody StudentStatusDTO studentStatusDTO) {
        FrameworkUtils.notImplemented();// TODO
    }

    @DeleteMapping("/course/{id}/student/{studentId}")
    public void removeStudentFromCourse(@PathVariable Long id,
                                        @PathVariable Long studentId) {
        FrameworkUtils.notImplemented();// TODO
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
    @Getter
    public static class StudentStatusList {
        private List<StudentWithEnrollStatus> content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentList {
        private List<Long> studentList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentStatusDTO {
        private Enrollment.EnrollmentType status;
    }
}
