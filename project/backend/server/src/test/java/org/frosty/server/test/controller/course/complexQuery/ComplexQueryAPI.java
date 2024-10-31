package org.frosty.server.test.controller.course.complexQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CourseComplexQueryController;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@RequiredArgsConstructor
public class ComplexQueryAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String courseBaseUrl = PathConstant.API + "/course";

    public ResultActions getCourseAllStructureInfo(String token, Long courseId) throws Exception {
        String url = courseBaseUrl + "/" + courseId + "/all-structure-info";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public CourseComplexQueryController.StructureInfo getCourseAllStructureInfoSuccess(String token, Long courseId) throws Exception {
        var resp = getCourseAllStructureInfo(token, courseId)
                .andExpect(RespChecker.success())
                .andReturn();
        return objectMapper.readValue(resp.getResponse().getContentAsString(), CourseComplexQueryController.StructureInfo.class);
    }
}

