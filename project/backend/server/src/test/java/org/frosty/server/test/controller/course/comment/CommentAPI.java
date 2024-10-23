package org.frosty.server.test.controller.course.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;


@Component
@RequiredArgsConstructor
public class CommentAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthAPI authAPI;
    private final ResourceAPI resourceAPI;
    private final CommentAPI commentAPI;
    private final String commentBaseUrl = PathConstant.API +"/resource";

    public ResourceComment getResourceComment(Long resourceId)  {
        return new ResourceComment();

    }
}
