package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.services.course.CourseLikeService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(PathConstant.API + "/course")
@RequiredArgsConstructor
public class CourseLikeController {
    private final CourseLikeService courseLikeService;

    // 点赞课程
    @PostMapping("/{courseId}/like")
    public Response createCourseLike(@PathVariable Long courseId, Long userId) {
        // 逻辑处理
        courseLikeService.createCourseLike(courseId, userId);
        return Response.getSuccess("like successfully");
    }

    // 取消点赞课程
    @DeleteMapping("/{courseId}/like")
    public Response deleteCourseLike(@PathVariable Long courseId, Long userId) {
        courseLikeService.deleteCourseLike(courseId, userId);
        return Response.getSuccess("cancel like successfully");
        // 逻辑处理
    }

    // 检查用户是否点赞了课程
    @GetMapping("/{courseId}/like")
    public Map<String,Boolean> checkCourseLike(@PathVariable Long courseId, Long userId) {
        if(true) throw new NotImplementedException();
        return Map.of("is_like",true);
//        return courseLikeService.checkCourseLike(courseId, userId);
        // 逻辑处理
    }
}
