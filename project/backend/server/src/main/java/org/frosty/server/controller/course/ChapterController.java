package org.frosty.server.controller.course;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.services.course.ChapterService;
import org.frosty.server.services.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.frosty.server.entity.bo.User.Role.student;


@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;
    private final UserService userService;

    // 创建章节
    @PostMapping("/course/{courseId}/chapter")
    public void createChapter(@RequestBody Chapter chapter) {
        chapter.setChapterId(null);
        chapterService.createChapter(chapter);
    }

    // 获取章节
    // ？？？理论上来说，前端是通过接受用户的点击或者其他的请求来向后端要数据吧，那么我们还有必要去判断这个chapter是否真实存在吗？毕竟能出现在前端的chapter是不是都会在数据库中？
    @GetMapping("/chapter/{id}")
    public Chapter getChapter(@PathVariable Long id) {
        return chapterService.findByID(id);
    }

    // 更新章节
    @PutMapping("/chapter/{id}")
    public Response updateChapter(@PathVariable Long id, @RequestBody Chapter updatedChapter) {
        updatedChapter.setChapterId(id);
        chapterService.updateChapter(id, updatedChapter);
        return Response.getSuccess("");
    }


    // 删除章节
    @DeleteMapping("/chapter/{id}")
    public void deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
    }

    // 获取全部章节
    @GetMapping("/course/{id}/chapter")
    public Map<String, List<Chapter>> getAll(@GetToken TokenInfo tokenInfo, @PathVariable Long id) {
        long userID = tokenInfo.getAuthInfo().getUserID();
        List<Chapter> chapters = new LinkedList<>();

        // TODO 对于入课状态为 pending-approved的学生不得返回 non-public
        // 这样子写可以吗？
        if (userService.findById(userID).getRole().equals(student)) {
            chapters = chapterService.getAllChaptersForStudentByCourseId(userID,id);
        } else {
            chapters = chapterService.getAllChaptersByCourseId(id);
        }


        return Map.of("content", chapters);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterList {
        List<Chapter> content;
    }
}

