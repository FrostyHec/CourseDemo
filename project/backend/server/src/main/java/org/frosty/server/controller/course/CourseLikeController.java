package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.services.course.CourseLikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstant.API + "/course/like")
@RequiredArgsConstructor
public class CourseLikeController {
    private final CourseLikeService courseLikeService;

    // 点赞课程
    @PostMapping()
    public Response createCourseLike(Long courseId, Long userId) {
        // 逻辑处理
        courseLikeService.createCourseLike(courseId, userId);
        return Response.getSuccess("like successfully");
    }

    // 取消点赞课程
    @DeleteMapping()
    public Response deleteCourseLike(Long courseId, Long userId) {
        courseLikeService.deleteCourseLike(courseId, userId);
        return Response.getSuccess("cancel like successfully");
        // 逻辑处理
    }

    // 检查用户是否点赞了课程
    @GetMapping()
    public boolean checkCourseLike(Long courseId, Long userId) {
        return courseLikeService.checkCourseLike(courseId, userId);
        // 逻辑处理
    }


}
