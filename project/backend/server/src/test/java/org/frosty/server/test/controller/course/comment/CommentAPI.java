package org.frosty.server.test.controller.course.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
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
public class CommentAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final ResourceAPI resourceAPI;
    private final String commentBaseUrl = PathConstant.API + "/resource";

    public ResourceComment getTemplateComment(int resourceId) {
        return new ResourceComment()
                .setResourceId(resourceId)
                .setUserId(1)
                .setCommentText("Sample comment text");
    }

    public ResourceComment getTemplateReplyComment(int resourceId, Long commentReplyId) {
        return new ResourceComment()
                .setResourceId(resourceId)
                .setUserId(1)
                .setCommentText("Sample comment text")
                .setCommentReply(commentReplyId);
    }

    public ResultActions create(String token, Long resourceId, ResourceComment comment) throws Exception {
        String url = commentBaseUrl + "/" + resourceId + "/comment";
        String json = objectMapper.writeValueAsString(comment);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void createSuccess(String token, Long resourceId, ResourceComment comment) throws Exception {
        create(token, resourceId, comment)
                .andExpect(RespChecker.success());
    }

    public ResultActions createReply(String token, Long commentId, ResourceComment comment) throws Exception {
        String url = commentBaseUrl + "/comment" + "/" + commentId + "/comment";
        String json = objectMapper.writeValueAsString(comment);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void createReplySuccess(String token, Long resourceId, ResourceComment comment) throws Exception {
        create(token, resourceId, comment)
                .andExpect(RespChecker.success());
    }

    public ResultActions update(String token, Long commentId, ResourceComment updatedComment) throws Exception {
        String url = commentBaseUrl + "/comment/" + commentId;
        String json = objectMapper.writeValueAsString(updatedComment);
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateSuccess(String token, Long commentId, ResourceComment updatedComment) throws Exception {
        update(token, commentId, updatedComment)
                .andExpect(RespChecker.success());
    }

    public ResultActions delete(String token, Long commentId) throws Exception {
        String url = commentBaseUrl + "/comment/" + commentId;
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void deleteSuccess(String token, Long commentId) throws Exception {
        delete(token, commentId)
                .andExpect(RespChecker.success());
    }

    public ResultActions get(String token, Long commentId) throws Exception {
        String url = commentBaseUrl + "/comment/" + commentId;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResourceComment getSuccess(String token, Long commentId) throws Exception {
        var resp = get(token, commentId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, ResourceComment.class);
    }

    public ResultActions getAll(String token, Long resourceId) throws Exception {
        String url = commentBaseUrl + "/" + resourceId + "/comments";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<ResourceComment> getAllSuccess(String token, Long resourceId) throws Exception {
        var resp = getAll(token, resourceId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, List.class);
    }
}
