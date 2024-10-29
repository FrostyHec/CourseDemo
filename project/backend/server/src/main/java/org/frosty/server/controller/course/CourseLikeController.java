package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.services.course.CourseLikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstant.API + "/course")
@RequiredArgsConstructor
public class CourseLikeController {
    private final CourseLikeService courseLikeService;

    // 点赞课程
    @PostMapping("/{courseId}/like")
    public void createCourseLike(@PathVariable Long courseId, Long userId) {
        if (courseLikeService.checkCourseLike(courseId, userId)) {
            return;
//            throw new ExternalException(Response.getBadRequest("already-like"));
        }
        courseLikeService.createCourseLike(courseId, userId);
    }

    // 取消点赞课程
    @DeleteMapping("/{courseId}/like")
    public void deleteCourseLike(@PathVariable Long courseId, Long userId) {
        courseLikeService.deleteCourseLike(courseId, userId);
    }

    // 检查用户是否点赞了课程
    @GetMapping("/{courseId}/like")
    public boolean checkCourseLike(@PathVariable Long courseId, Long userId) {
        return courseLikeService.checkCourseLike(courseId, userId);
    }
}
