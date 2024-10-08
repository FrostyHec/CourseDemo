package org.frosty.demo.controller;


import lombok.RequiredArgsConstructor;

import org.frosty.demo.config.CommonConstant;
import org.frosty.demo.entity.dto.Chapter;
import org.frosty.demo.services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(CommonConstant.API_VERSION + "/course/{courseId}")
@RequiredArgsConstructor
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    // 创建章节
    @PostMapping("/chapter")
    public Map<String,String> createChapter(@RequestBody Chapter chapter) {
        try {
            chapterService.createChapter(chapter);
            return Map.of("say","Successfully created course.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 获取章节
    @GetMapping("/chapter/{id}")
    public Chapter getChapter(@PathVariable Long id) {
        try {
            Chapter chapter = chapterService.findByID(id);
            return chapter; // Chapter not found
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    // 更新章节
    @PutMapping("/chapter/{id}")
    public Map<String,String> updateChapter(@PathVariable Long id, @RequestBody Chapter updatedChapter) {
        chapterService.updateChapter(updatedChapter);
        return Map.of("say","Successfully updated course.");
    }



    // 删除章节
    @DeleteMapping("/chapter/{id}")
    public Map<String,String> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return Map.of("say","Successfully deleted course.");
    }

    // 获取全部章节
    @GetMapping("/chapter")
    public List<Chapter> getAllChapters() {
        List<Chapter> chapters = chapterService.getAll();
        return chapters;
    }
}

