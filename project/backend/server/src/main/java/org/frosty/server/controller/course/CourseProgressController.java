package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseProgressController {

    @PutMapping("/resource/{id}/study/complete")
    public void completeResource(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
    }

    @PutMapping("/chapter/{id}/study/complete")
    public void completeChapter(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
    }

    @PutMapping("/course/{id}/study/complete")
    public void completeCourse(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
    }

    @GetMapping("/course/{id}/study")
    public ChapterProgress checkCourseProgress(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
        return null;
    }
    @PutMapping("/course/{id}/study/all-clear")
    public void clearAllStudentCourseProgress(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
    }

    @PutMapping("/chapter/{id}/study/all-clear")
    public void clearAllStudentChapterProgress(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
    }

    @PutMapping("/resource/{id}/study/all-clear")
    public void clearAllStudentResourceProgress(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseProgress{
        public Long courseId;
        public List<ChapterProgress> chapterProgress;
        public Boolean isCompleted;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterProgress{
        public Long chapterId;
        public List<ResourceProgress> videoResources;
        public Boolean isCompleted;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResourceProgress{
        public Long resourceId;
        public Boolean isCompleted;
    }
}
