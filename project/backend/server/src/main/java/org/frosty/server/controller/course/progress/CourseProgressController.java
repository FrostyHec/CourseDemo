package org.frosty.server.controller.course.progress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.AuthEx;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.services.course.progress.CourseProgressService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseProgressController {
    private final CourseProgressService courseProgressService;

    @PutMapping("/resource/{rid}/study/complete")
    public void completeResource(@GetToken TokenInfo tokenInfo, @PathVariable Long rid) {
        AuthInfo auth = AuthEx.checkPass(tokenInfo);
        courseProgressService.completeResource(rid,auth);
    }

    @PutMapping("/chapter/{cid}/study/complete")
    public void completeChapter(@GetToken TokenInfo tokenInfo, @PathVariable Long cid) {
        AuthInfo auth = AuthEx.checkPass(tokenInfo);
        courseProgressService.completeChapter(cid,auth);
    }

    @PutMapping("/course/{csid}/study/complete")
    public void completeCourse(@GetToken TokenInfo tokenInfo, @PathVariable Long csid) {
        AuthInfo auth = AuthEx.checkPass(tokenInfo);
        courseProgressService.completeCourse(csid,auth);
    }

    @GetMapping("/course/{id}/study")
    public ChapterProgress checkCourseProgress(@GetToken TokenInfo tokenInfo, @PathVariable String id) {
        FrameworkUtils.notImplemented();
        return null;
    }
    @PutMapping("/course/{csid}/study/all-clear")
    public void clearAllStudentCourseProgress(@GetToken TokenInfo tokenInfo, @PathVariable  Long csid) {
        var auth = AuthEx.checkPass(tokenInfo);
        courseProgressService.clearAllStudentCourseProgress(csid,auth);
    }

    @PutMapping("/chapter/{cid}/study/all-clear")
    public void clearAllStudentChapterProgress(@GetToken TokenInfo tokenInfo, @PathVariable Long cid) {
        FrameworkUtils.notImplemented();
    }

    @PutMapping("/resource/{rid}/study/all-clear")
    public void clearAllStudentResourceProgress(@GetToken TokenInfo tokenInfo, @PathVariable Long rid) {
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
