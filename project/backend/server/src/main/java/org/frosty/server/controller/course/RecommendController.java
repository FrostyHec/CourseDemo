package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API+"/recommend")
@RequiredArgsConstructor
public class RecommendController {
    // TODO HLH

    @GetMapping("/courses/hot")
    public Map<String, List<CourseWithStudentCount>> getHotCourses(int page_size,int page_num) {
        List<CourseWithStudentCount> res =null;
        FrameworkUtils.notImplemented();
        return Map.of("content", res);
    }
    @GetMapping("/teachers/hot")
    public Map<String, List<CourseWithStudentCount>> getHotTeachers(int page_size,int page_num) {
        List<CourseWithStudentCount> res =null;
        FrameworkUtils.notImplemented();
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
