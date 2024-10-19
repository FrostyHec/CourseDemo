package org.frosty.object_storage.controller.internal_storage;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InternalStorageAPI {
    private final MockMvc mockMvc;
    private final String baseUrl = PathConstant.INTERNAL_API + "/storage";

    public MockMultipartFile getTemplateMockFileTemplate() throws IOException, URISyntaxException {
        return getTemplateMockFile(1);
    }

    public MockMultipartFile getTemplateMockFile(int idx) throws IOException, URISyntaxException {
        final String templateFilePath = "test_template";

        ClassLoader classLoader = getClass().getClassLoader();
        String resourcePath = templateFilePath + "_" + idx + ".txt";
        Path path = Path.of(Objects.requireNonNull(classLoader.getResource(resourcePath)).toURI());
        String originalFileName = path.getFileName().toString();
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile("file", originalFileName, MediaType.APPLICATION_OCTET_STREAM_VALUE, content);
    }

    public ResultActions uploadFile(String key, MockMultipartFile file) throws Exception {
        String url = baseUrl + "/" + key;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(file.getInputStream().readAllBytes()); // 读取输入流内容

        return mockMvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON));
    }

    public void uploadFileSuccess(String key, MockMultipartFile file) throws Exception {
        uploadFile(key, file)
                .andExpect(RespChecker.success());
    }

    public ResultActions getFile(String key) throws Exception {
        String url = baseUrl + "/" + key;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_OCTET_STREAM));
    }

    public byte[] getFileSuccess(String key) throws Exception {
        MockHttpServletResponse response = getFile(key).andReturn().getResponse();

        // 检查响应状态
        if (response.getStatus() == 200) {
            // 返回文件的字节内容
            return response.getContentAsByteArray();
        } else {
            throw new RuntimeException("Failed to get file: " + response.getStatus());
        }
    }


    public ResultActions deleteFile(String key) throws Exception {
        String url = baseUrl + "/" + key;
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void deleteFileSuccess(String key) throws Exception {
        deleteFile(key)
                .andExpect(RespChecker.success());
    }

    public ResultActions checkFileExist(String key) throws Exception {
        String url = baseUrl + "/" + key + "/exists";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON));
    }

    public boolean checkFileExistSuccess(String key) throws Exception {
        var resp = checkFileExist(key)
                .andExpect(RespChecker.success())
                .andReturn();
        return (Boolean) JsonUtils.toMapData(resp).get("exists");
    }

    public ResultActions getAccessKey(String key, String caseName) throws Exception {
        String url = baseUrl + "/" + key + "/access-key";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .param("case_name", caseName)
                .accept(MediaType.APPLICATION_JSON));
    }

    public String getAccessKeySuccess(String key, String caseName) throws Exception {
        var resp = getAccessKey(key, caseName)
                .andExpect(RespChecker.success())
                .andReturn();
        return (String) JsonUtils.toMapData(resp).get("access_key");
    }

    public ResultActions withdrawAccessKey(String key, String caseName) throws Exception {
        String url = baseUrl + "/" + key + "/access-key";
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .param("case_name", caseName)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void withdrawAccessKeySuccess(String key, String caseName) throws Exception {
        withdrawAccessKey(key, caseName)
                .andExpect(RespChecker.success());
    }
}
