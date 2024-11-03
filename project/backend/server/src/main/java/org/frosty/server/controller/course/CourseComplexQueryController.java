package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.*;
import org.frosty.server.services.course.CourseComplexQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CourseComplexQueryController {
    private final CourseComplexQueryService courseComplexQueryService;


//    // 这样写也太丑了吧？？？
//    @GetMapping("/course/{id}/all-structure-info")
//    public StructureInfo getCourseAllStructureInfo(@PathVariable Long id) {
//        Course course = courseComplexQueryService.findCourseById(id);
//        List<ChaptersWithContentInfo> chaptersWithContentInfos = new ArrayList<>();
//        List<Chapter> chapters =  courseComplexQueryService.findChapterByCourseId(id);
//        for (Chapter chapter : chapters) {
//            ChaptersWithContentInfo chaptersWithContentInfo = new ChaptersWithContentInfo();
//            Long chapterId = chapter.getChapterId();
//            List<Assignment> assignments = courseComplexQueryService.findAssByChapterId(chapterId);
//            List<Resource> resources = courseComplexQueryService.findResourceByChapterId(chapterId);
//            List<ChapterContentInfo> chapterContentInfos = new ArrayList<>();
//            for (Assignment assignment : assignments) {
//                ChapterContentInfo chapterContentInfo = new ChapterContentInfo();
//                chapterContentInfo.type = ChapterContent.ChapterContentType.assignment;
//                chapterContentInfo.metadata = assignment;
//                chapterContentInfos.add(chapterContentInfo);
//            }
//            for (Resource resource : resources) {
//                ChapterContentInfo chapterContentInfo = new ChapterContentInfo();
//                chapterContentInfo.type = ChapterContent.ChapterContentType.resource;
//                chapterContentInfo.metadata = resource;
//                chapterContentInfos.add(chapterContentInfo);
//            }
//            chaptersWithContentInfo.chapterInfo = chapter;
//            chaptersWithContentInfo.content = chapterContentInfos;
//            chaptersWithContentInfos.add(chaptersWithContentInfo);
//        }
//        StructureInfo structureInfo = new StructureInfo();
//        structureInfo.courseInfo = course;
//        structureInfo.chapters = chaptersWithContentInfos;
//        return structureInfo;
//    }


    // 请教了一下GPT，不知道下面这个跑起来效果一不一样？
    @GetMapping("/course/{id}/all-structure-info")
    public StructureInfo getCourseAllStructureInfo(@PathVariable Long id) {
        Course course = courseComplexQueryService.findCourseById(id);

        // 获取章节信息并封装为 ChaptersWithContentInfo
        List<ChaptersWithContentInfo> chaptersWithContentInfos = courseComplexQueryService.findChapterByCourseId(id)
                .stream()
                .map(chapter -> {
                    Long chapterId = chapter.getChapterId();

                    // 获取当前章节的作业和资源
                    List<ChapterContentInfo> chapterContentInfos = Stream.concat(
                            courseComplexQueryService.findAssByChapterId(chapterId)
                                    .stream()
                                    .map(assignment -> new ChapterContentInfo(ChapterContent.ChapterContentType.assignment, assignment)),
                            courseComplexQueryService.findResourceByChapterId(chapterId)
                                    .stream()
                                    .map(resource -> new ChapterContentInfo(ChapterContent.ChapterContentType.resource, resource))
                    ).collect(Collectors.toList());

                    // 返回 ChaptersWithContentInfo
                    return new ChaptersWithContentInfo(chapter, chapterContentInfos);
                })
                .collect(Collectors.toList());

        // 返回最终的 StructureInfo
        return new StructureInfo(course, chaptersWithContentInfos);
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
        private Chapter chapterInfo;
        private List<ChapterContentInfo> content;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterContentInfo {
        private ChapterContent.ChapterContentType type;
        private ChapterContent metadata;
    }

}
