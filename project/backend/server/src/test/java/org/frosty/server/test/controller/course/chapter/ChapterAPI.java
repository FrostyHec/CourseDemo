package org.frosty.server.test.controller.course.chapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.ChapterController;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.Chapter.ChapterType;
import org.frosty.server.mapper.course.ChapterMapper;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChapterAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final CourseAPI courseAPI;
    private final ChapterMapper chapterMapper;
    private final String courseBaseUrl = PathConstant.API + "/course";
    private final String chapterBaseUrl = PathConstant.API + "/chapter";

    public Chapter getTemplateTeachingChapter(Long courseId) {
        return new Chapter()
                .setCourseId(courseId)
                .setChapterTitle("Chapter Title")
                .setCourseId(courseId)
                .setChapterType(ChapterType.teaching)
                .setContent("Chapter Content")
                .setChapterOrder(1);
    }

    public ResultActions create(String token, Long courseId, Chapter chapter) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/chapter";
        String json = objectMapper.writeValueAsString(chapter);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void createSuccess(String token, Long courseId, Chapter chapter) throws Exception {
        create(token, courseId, chapter)
                .andExpect(RespChecker.success());
    }

    public ResultActions update(String token, Long chapterId, Chapter chapter) throws Exception {
        String url = chapterBaseUrl + "/" + chapterId;
        String json = objectMapper.writeValueAsString(chapter);
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateSuccess(String token, Long chapterId, Chapter chapter) throws Exception {
        update(token, chapterId, chapter)
                .andExpect(RespChecker.success());
    }

    public ResultActions delete(String token, Long chapterId) throws Exception {
        String url = chapterBaseUrl + "/" + chapterId;
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void deleteSuccess(String token, Long chapterId) throws Exception {
        delete(token, chapterId)
                .andExpect(RespChecker.success());
    }

    public ResultActions get(String token, Long chapterId) throws Exception {
        String url = chapterBaseUrl + "/" + chapterId;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public Chapter getSuccess(String token, Long chapterId) throws Exception {
        var resp = get(token, chapterId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, Chapter.class);
    }

    public ResultActions getAll(String token, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/chapter";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<Chapter> getAllSuccess(String token, Long courseId) throws Exception {
        var resp = getAll(token, courseId)
                .andExpect(RespChecker.success())
                .andReturn();
        // !!!!!!!!!!!!!!!
        return JsonUtils.toObject(resp, ChapterController.ChapterList.class).getContent();// 这个非常非常重要！！！
        // !!!!!!!!!!!!!!!
    }

    public Long addTestCourseTestChapterAndGetId(Long uid) {
        var coId = courseAPI.addTestCourseAndGetId(uid);
        var e = getTemplateTeachingChapter(coId);
        chapterMapper.insert(e);
        assert e.getChapterId() != null;
        return e.getChapterId();
    }
}
