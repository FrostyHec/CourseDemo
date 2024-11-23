package org.frosty.server.controller.course.progress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.AuthEx;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.services.course.progress.CourseProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseProgressController {
    private final CourseProgressService courseProgressService;

    @PutMapping("/resource/{rid}/study/complete")
    public void completeResource(@GetPassedToken AuthInfo auth, @PathVariable Long rid) {
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

    @GetMapping("/course/{csid}/study")
    public CourseProgress checkCourseProgress(@GetToken TokenInfo tokenInfo, @PathVariable Long csid) {
        AuthInfo auth = AuthEx.checkPass(tokenInfo);
        return courseProgressService.queryCourseProgress(csid,auth);
    }
    @PutMapping("/course/{csid}/study/all-clear")
    public void clearAllStudentCourseProgress(@GetToken TokenInfo tokenInfo, @PathVariable  Long csid) {
        var auth = AuthEx.checkPass(tokenInfo);
        courseProgressService.clearAllStudentCourseProgress(csid,auth);
    }

    @PutMapping("/chapter/{cid}/study/all-clear")
    public void clearAllStudentChapterProgress(@GetToken TokenInfo tokenInfo, @PathVariable Long cid) {
        var auth = AuthEx.checkPass(tokenInfo);
        courseProgressService.clearAllStudentChapterProgress(cid,auth);
    }

    @PutMapping("/resource/{rid}/study/all-clear")
    public void clearAllStudentResourceProgress(@GetToken TokenInfo tokenInfo, @PathVariable Long rid) {
        var auth = AuthEx.checkPass(tokenInfo);
        courseProgressService.clearAllStudentResourceProgress(rid,auth);
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseProgress{
        private Long courseId;
        private List<ChapterProgress> chapterProgress;
        private Boolean isCompleted;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterProgress{
        private Long chapterId;
        private List<ResourceProgress> videoResources;
        private Boolean isCompleted;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResourceProgress{
        private Long resourceId;
        private Boolean isCompleted;
    }
}
