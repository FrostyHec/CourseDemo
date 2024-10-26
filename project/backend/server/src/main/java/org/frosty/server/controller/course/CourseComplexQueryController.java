package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.ChapterContent;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseComplexQueryController {
    @GetMapping("/course/{id}/all-structure-info")
    public StructureInfo getCourseAllStructureInfo(@PathVariable String id) {
        FrameworkUtils.notImplemented();
        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StructureInfo {
        private Course courseInfo;
        private List<ChaptersWithContentInfo> chapters;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChaptersWithContentInfo {
        private Course courseInfo;
        private List<ChapterContent> content;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterContentInfo {
        private ChapterContent.ChapterContentType type;
        private ChapterContent metadata;
    }

}
