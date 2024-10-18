package org.frosty.object_storage.controller.public_access;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Component
@RequiredArgsConstructor
public class PublicAccessAPI {
    private final MockMvc mockMvc;
    private final String baseUrl = PathConstant.API + "/storage";

    public ResultActions getFileFromPublic(String case_name, String access_key, String objName) throws Exception {
        String url = baseUrl + "/" + objName;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .param("case_name",case_name)
                .param("access_key",access_key)
                .accept(MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_JSON)
        );
    }

    public byte[] getFileFromPublicSuccess(String case_name, String access_key, String objName) throws Exception {
        return getFileFromPublic(case_name, access_key, objName)
                .andExpect(MockMvcResultMatchers.status().isOk()) // HTTP状态码为200
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
    }
}
