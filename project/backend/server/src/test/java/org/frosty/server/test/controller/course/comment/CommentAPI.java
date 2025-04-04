package org.frosty.server.test.controller.course.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.entity.bo.CommentResource;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class CommentAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final ResourceAPI resourceAPI;
    private final String commentBaseUrl = PathConstant.API + "/resource";

    public ResourceComment getTemplateComment(Long resourceId, Long userId) {
        return new ResourceComment()
                .setResourceId(resourceId)
                .setUserId(userId)
                .setCommentText("Sample comment text");
    }

    public CommentResource getTemplateCommentResourceMetadata(Long commentId,String suffix) {
        return new CommentResource()
                .setCommentId(commentId)
                .setResourceName("test")
                .setSuffix(suffix);
    }


    public MockMultipartFile loadTemplateFile(String name) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String relUri = "./resource/" + name;// TODO
        Path path = Path.of(Objects.requireNonNull(classLoader.getResource(relUri)).toURI());
        String originalFileName = path.getFileName().toString();
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile("file", originalFileName, MediaType.MULTIPART_FORM_DATA_VALUE, content);

    }

    public ResourceComment getTemplateReplyComment(Long resourceId, Long commentReplyId, Long userId) {
        return new ResourceComment()
                .setResourceId(resourceId)
                .setUserId(userId)
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

    public List<CommentController.CommentWithUserAndFileAndAccessKey> getAllSuccess(String token, Long resourceId) throws Exception {
        var resp = getAll(token, resourceId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, CommentController.CommentList.class).getContent();
    }

    public ResultActions uploadFiles(String token, CommentResource commentResource,Long commentId, MockMultipartFile file) throws Exception {
        String url = commentBaseUrl + "/comment/" + commentId + "/file";
        String json = objectMapper.writeValueAsString(commentResource);
        MockMultipartFile jsonFile = new MockMultipartFile("data", "", "application/json", json.getBytes());
        return mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
                .file(jsonFile)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void uploadFilesSuccess(String token, CommentResource commentResource, Long commentId, MockMultipartFile file) throws Exception {
        uploadFiles(token, commentResource, commentId, file)
                .andExpect(RespChecker.success());
    }

    public ResultActions removeFiles(String token,Long commentId, Long fileId) throws Exception {
        String url = commentBaseUrl  + "/comment/" + commentId + "/file/" + fileId;
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void removeFilesSuccess(String token,Long commentId, Long fileId) throws Exception {
        removeFiles(token,commentId, fileId)
                .andExpect(RespChecker.success());
    }

    public void checkCommentResourceMetadata(CommentResource rcvdCommentResource, CommentResource originCommentResource) {
        assert rcvdCommentResource.getCommentId()!=null;
        assert rcvdCommentResource.getFileName()!=null;
        assert Objects.equals(rcvdCommentResource.getResourceName(), originCommentResource.getResourceName());
        assert Objects.equals(rcvdCommentResource.getSuffix(), originCommentResource.getSuffix());
    }
}
