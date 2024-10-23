package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.services.course.CourseLikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstant.API + "/course")
@RequiredArgsConstructor
public class CourseLikeController {
    private final CourseLikeService courseLikeService;

    // 点赞课程
    @PostMapping("/{courseId}/like")
    public Response createCourseLike(@PathVariable Long courseId, Long userId) {
        if (courseLikeService.checkCourseLike(courseId, userId)) {
            courseLikeService.createCourseLike(courseId, userId);
            return Response.getSuccess("like successfully");
        }
        return Response.getBadRequest("already like");
    }

    // 取消点赞课程
    @DeleteMapping("/{courseId}/like")
    public Response deleteCourseLike(@PathVariable Long courseId, Long userId) {
        courseLikeService.deleteCourseLike(courseId, userId);
        return Response.getSuccess("cancel like successfully");
    }

    // 检查用户是否点赞了课程
    @GetMapping("/{courseId}/like")
    public Response checkCourseLike(@PathVariable Long courseId, Long userId) {
        return Response.getSuccess(courseLikeService.checkCourseLike(courseId, userId));
    }
}
