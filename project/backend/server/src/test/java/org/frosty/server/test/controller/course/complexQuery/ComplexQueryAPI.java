package org.frosty.server.test.controller.course.complexQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.course.CourseComplexQueryController;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.ChapterContent;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.mapper.course.AssignmentMapper;
import org.frosty.server.mapper.course.ChapterMapper;
import org.frosty.server.mapper.course.CourseMapper;
import org.frosty.server.mapper.course.ResourceMapper;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.server.test.controller.course.assignment.AssignmentAPI;
import org.frosty.server.test.controller.course.chapter.ChapterAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.test_common.utils.JsonUtils;
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

    private final CourseAPI courseAPI;
    private final ChapterAPI chapterAPI;
    private final ResourceAPI resourceAPI;
    private final AssignmentAPI assignmentAPI;


    private final String courseBaseUrl = PathConstant.API + "/course";
    private final ChapterMapper chapterMapper;
    private final CourseMapper courseMapper;
    private final ResourceMapper resourceMapper;
    private final AssignmentMapper assignmentMapper;

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
        return JsonUtils.toObject(resp, CourseComplexQueryController.StructureInfo.class);
//        return objectMapper.readValue(resp.getResponse().getContentAsString(), CourseComplexQueryController.StructureInfo.class);
    }

    public void addComplex(Long uid){
        var coId = courseAPI.addTestCourseAndGetId(uid);
        // 创建第一个课程，只有resource
        var chapter1 = chapterAPI.getTemplateTeachingChapter(coId);
        chapterMapper.insert(chapter1);
        var ch1id = chapter1.getChapterId();
        var resource11 = resourceAPI.getTemplateResource(ch1id, "test", "pdf", Resource.ResourceType.courseware);
        resourceMapper.insert(resource11);
        var resource12 = resourceAPI.getTemplateResource(ch1id, "test", "pdf", Resource.ResourceType.courseware);
        resourceMapper.insert(resource12);
        var resource13 = resourceAPI.getTemplateResource(ch1id, "test", "pdf", Resource.ResourceType.courseware);
        resourceMapper.insert(resource13);

        //创建第二个课程，只有ass
        var chapter2 = chapterAPI.getTemplateTeachingChapter(coId);
        chapterMapper.insert(chapter2);
        var ch2id = chapter2.getChapterId();
        var ass21 = assignmentAPI.getTemplateAssignment(ch2id);
        assignmentMapper.insert(ass21);
        var ass22 = assignmentAPI.getTemplateAssignment(ch2id);
        assignmentMapper.insert(ass22);
        var ass23 = assignmentAPI.getTemplateAssignment(ch2id);
        assignmentMapper.insert(ass23);

        // 创建第三个课程，两种资源都有
        var chapter3 = chapterAPI.getTemplateTeachingChapter(coId);
        chapterMapper.insert(chapter3);
        var ch3id = chapter3.getChapterId();
        var resource31 = resourceAPI.getTemplateResource(ch3id, "test", "pdf", Resource.ResourceType.courseware);
        resourceMapper.insert(resource31);
        var resource32 = resourceAPI.getTemplateResource(ch3id, "test", "pdf", Resource.ResourceType.courseware);
        resourceMapper.insert(resource32);
        var resource33 = resourceAPI.getTemplateResource(ch3id, "test", "pdf", Resource.ResourceType.courseware);
        resourceMapper.insert(resource33);
        var ass31 = assignmentAPI.getTemplateAssignment(ch3id);
        assignmentMapper.insert(ass31);
        var ass32 = assignmentAPI.getTemplateAssignment(ch3id);
        assignmentMapper.insert(ass32);
        var ass33 = assignmentAPI.getTemplateAssignment(ch3id);
        assignmentMapper.insert(ass33);
    }
}

