package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
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
    public void createCourseLike(@GetToken TokenInfo tokenInfo, @PathVariable Long courseId) {
        long userId = tokenInfo.getAuthInfo().getUserID();
        if (courseLikeService.checkCourseLike(courseId, userId)) {
            return;
//            throw new ExternalException(Response.getBadRequest("already-like"));
        }
        courseLikeService.createCourseLike(courseId, userId);
    }

    // 取消点赞课程
    @DeleteMapping("/{courseId}/like")
    public void deleteCourseLike(@GetToken TokenInfo tokenInfo, @PathVariable Long courseId) {
        long userId = tokenInfo.getAuthInfo().getUserID();
        courseLikeService.deleteCourseLike(courseId, userId);
    }

    // 检查用户是否点赞了课程
    @GetMapping("/{courseId}/like")
    public Map<String, Boolean> checkCourseLike(@GetToken TokenInfo tokenInfo, @PathVariable Long courseId) {
        long userId = tokenInfo.getAuthInfo().getUserID();
        return Map.of("is_like", courseLikeService.checkCourseLike(courseId, userId));
    }
}
