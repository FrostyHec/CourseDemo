package org.frosty.server.controller.course;


import lombok.RequiredArgsConstructor;

import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Chapter;
import org.springframework.web.bind.annotation.*;
import org.frosty.server.services.ChapterService;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(PathConstant.API + "/course/{courseId}")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    // 创建章节
    @PostMapping("/chapter")
    public Map<String,String> createChapter(@RequestBody Chapter chapter) {
            chapterService.createChapter(chapter);
            return Map.of("say","Successfully created course.");
    }

    // 获取章节
    // ？？？理论上来说，前端是通过接受用户的点击或者其他的请求来向后端要数据吧，那么我们还有必要去判断这个chapter是否真实存在吗？毕竟能出现在前端的chapter是不是都会在数据库中？
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

