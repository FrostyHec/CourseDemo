package org.frosty.server.test.controller.course.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.entity.po.ResourceWithAccessKey;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.server.mapper.course.ResourceMapper;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.tools.CommonCheck;
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
public class ResourceAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String resourceBaseUrl = PathConstant.API + "/resource";
    private final String chapterBaseUrl = PathConstant.API + "/chapter";
    private final ChapterAPI chapterAPI;
    private final ResourceMapper resourceMapper;

    public MockMultipartFile loadTemplateFile(String name) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String relUri = "./resource/" + name;// TODO
        Path path = Path.of(Objects.requireNonNull(classLoader.getResource(relUri)).toURI());
        String originalFileName = path.getFileName().toString();
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile("file", originalFileName, MediaType.MULTIPART_FORM_DATA_VALUE, content);

    }

    public Resource getTemplateResource(Long chapterId,
                                        String resourceName,
                                        String suffix,
                                        Resource.ResourceType type
    ) {
        return new Resource()
                .setChapterId(chapterId)
                .setResourceName(resourceName)
                .setSuffix(suffix)
                .setFileName("TEST")
                .setResourceOrder(1)
                .setResourceVersionName("1.0")
                .setResourceVersionOrder(1)
                .setResourceType(type)
                .setStudentCanDownload(true);
    }

    public ResultActions uploadResource(String token, Long chapterId, Resource resource, MockMultipartFile file) throws Exception {
        String url = chapterBaseUrl + "/" + chapterId + "/resource";
        String json = objectMapper.writeValueAsString(resource);
        MockMultipartFile jsonFile = new MockMultipartFile("data", "", "application/json", json.getBytes());
        return mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
                .file(jsonFile)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void uploadResourceSuccess(String token, Long chapterId, Resource resource, MockMultipartFile file) throws Exception {
        uploadResource(token, chapterId, resource, file)
                .andExpect(RespChecker.success());
    }

    public ResultActions getResourceMetaData(String token, Long id) throws Exception {
        String url = resourceBaseUrl + "/" + id;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResourceWithAccessKey getResourceMetaDataSuccess(String token, Long id) throws Exception {
        var resp = getResourceMetaData(token, id)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, ResourceWithAccessKey.class);
    }

    public ResultActions updateResourceMetadata(String token, Long id, Resource updatedResource) throws Exception {
        String url = resourceBaseUrl + "/" + id;
        String json = objectMapper.writeValueAsString(updatedResource);
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateResourceMetadataSuccess(String token, Long id, Resource updatedResource) throws Exception {
        updateResourceMetadata(token, id, updatedResource)
                .andExpect(RespChecker.success());
    }

    public ResultActions deleteResource(String token, Long id) throws Exception {
        String url = resourceBaseUrl + "/" + id;
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void deleteResourceSuccess(String token, Long id) throws Exception {
        deleteResource(token, id)
                .andExpect(RespChecker.success());
    }

    public ResultActions getResourcesByChapter(String token, Long chapterId) throws Exception {
        String url = chapterBaseUrl + "/" + chapterId + "/resource";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<ResourceWithAccessKey> getResourcesByChapterSuccess(String token, Long chapterId) throws Exception {
        var resp = getResourcesByChapter(token, chapterId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, ResourceListResult.class).content;
    }

    public void checkSingle(Resource resource, List<Resource> li) {
        var e = CommonCheck.checkSingleAndGet(li);
        checkSingle(resource, e);
    }

    public void checkSingle(Resource resource, Resource e) {
        assert e.getChapterId().equals(resource.getChapterId());
        assert e.getResourceName().equals(resource.getResourceName());
        assert e.getSuffix().equals(resource.getSuffix());
    }

    public Long addTestCourseTestChapterTestResourceAndGetId(Long uid){
        var chapterId = chapterAPI.addTestCourseTestChapterAndGetId(uid);
        var e = getTemplateResource(chapterId,"test","pdf",Resource.ResourceType.courseware);
        resourceMapper.insert(e);
        assert e.getResourceId() != null;
        return e.getResourceId();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ResourceListResult {
        private List<ResourceWithAccessKey> content;
    }
}
