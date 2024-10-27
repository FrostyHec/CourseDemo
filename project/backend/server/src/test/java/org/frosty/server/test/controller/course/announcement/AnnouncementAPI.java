package org.frosty.server.test.controller.course.announcement;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.AnnouncementController;
import org.frosty.server.entity.bo.Announcement;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnnouncementAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String baseUrl = PathConstant.API;

    // Common performRequest method
    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder, String token, Object body) throws Exception {
        requestBuilder.headers(authUtil.setAuthHeader(token)).accept(MediaType.APPLICATION_JSON);
        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(json);
        }
        return mockMvc.perform(requestBuilder);
    }

    // Common success response handler
    public static <T> T getSuccessResponse(ResultActions resultActions, Class<T> responseType) throws Exception {
        var resp = resultActions.andExpect(RespChecker.success()).andReturn();
        return JsonUtils.toObject(resp, responseType);
    }

    public ResultActions createAnnouncement(String token, Long courseId, Announcement announcement) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                post(baseUrl + "/course/" + courseId+ "/announcement");
        return performRequest(requestBuilder, token, announcement);
    }

    public void createAnnouncementSuccess(String token, Long courseId, Announcement announcement) throws Exception {
        createAnnouncement(token, courseId, announcement).andExpect(RespChecker.success());
    }

    public ResultActions getAnnouncementsForCourse(String token, Long courseId) throws Exception {
        String url = PathConstant.API + "/course/" + courseId + "/announcement";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        return performRequest(requestBuilder, token, null);
    }

    public List<Announcement> getAnnouncementsForCourseSuccess(String token, Long courseId) throws Exception {
        var resultActions = getAnnouncementsForCourse(token, courseId);
        return getSuccessResponse(resultActions, AnnouncementController.AnnouncementList.class).getContent();
    }

    public ResultActions updateAnnouncement(String token, Long announcementId, Announcement announcement) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                put(baseUrl + "/announcement"  + announcementId);
        return performRequest(requestBuilder, token, announcement);
    }

    public void updateAnnouncementSuccess(String token, Long announcementId, Announcement announcement) throws Exception {
        updateAnnouncement(token, announcementId, announcement).andExpect(RespChecker.success());
    }

    public ResultActions deleteAnnouncement(String token, Long announcementId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                delete(baseUrl + "/announcement" + announcementId);
        return performRequest(requestBuilder, token, null);
    }

    public void deleteAnnouncementSuccess(String token, Long announcementId) throws Exception {
        deleteAnnouncement(token, announcementId).andExpect(RespChecker.success());
    }

    public ResultActions getAnnouncement(String token, Long announcementId) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                get(baseUrl + "/announcement" + announcementId);
        return performRequest(requestBuilder, token, null);
    }

    public void getAnnouncementSuccess(String token, Long announcementId) throws Exception {
        getAnnouncement(token, announcementId).andExpect(RespChecker.success());
    }
}