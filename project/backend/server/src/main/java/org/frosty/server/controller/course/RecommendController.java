package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.services.course.RecommendService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API + "/recommend")
@RequiredArgsConstructor
public class RecommendController {
    // TODO HLH

    private final RecommendService recommendService;

    @GetMapping("/courses/hot")
    public Map<String, List<CourseWithStudentCount>> getHotCourses(int page_size, int page_num) {
        // 公开/半公开的发布状态课程中，注册学生数最多的课程
        if (page_size < -1 || page_num < 0) {
            throw new ExternalException(Response.getBadRequest("Invalid page size or page number"));
        }
        List<CourseWithStudentCount> res = recommendService.getHotCourses(page_size, page_num);
        return Map.of("content", res);
    }

    @GetMapping("/teachers/hot")
    public Map<String, List<TeacherWithStudentCount>> getHotTeachers(int page_size, int page_num) {
        // 累积学生最多的老师
        if (page_size < -1 || page_num < 0) {
            throw new ExternalException(Response.getBadRequest("Invalid page size or page number"));
        }
        List<TeacherWithStudentCount> res = recommendService.getHotTeachers(page_size, page_num);
        return Map.of("content", res);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseWithStudentCount {
        private Course course;
        private Long studentNum; //已注册学生数
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeacherWithStudentCount {
        private UserPublicInfo teacher;
        private Long studentNum; //已注册学生数
    }

}
