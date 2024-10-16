package org.frosty.server.test.controller.smoke_test;

import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.controller.course.resource.ResourceAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@IdempotentControllerTest
public class ResourceSmokeTest {
    @Autowired
    private ResourceAPI resourceAPI;
    @Autowired
    private CourseAPI courseAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private ObjectStorageService oss;
    @Test
    public void testBasicCURD() throws Exception {
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;
        var uid = teacher.getUserId();
        var courseId = courseAPI.addTestCourseAndGetId(uid);
        //---test start---
        // upload resource
        var resource = resourceAPI.getTemplateResource(courseId,
                "test","pdf", Resource.ResourceType.courseware);
        var file = resourceAPI.loadTemplateFile("test.pdf");
        resourceAPI.uploadResourceSuccess(teacherToken, courseId, resource, file);

        // get resource metadata
        var li = resourceAPI.getResourcesByChapterSuccess(teacherToken,courseId);
        var rcvdResourceMetadata = CommonCheck.checkSingleAndGet(li);
        resourceAPI.checkSingle(resource, rcvdResourceMetadata);

        // get file by generated path
        var name = rcvdResourceMetadata.getFileName();
        var rvcdFile = oss.get(name,byte[].class);
        assert Arrays.equals(rvcdFile,file.getBytes());

        // update resource metadata
        resource.setResourceName("new name");
        resource.setResourceId(rcvdResourceMetadata.getResourceId());
        resourceAPI.updateResourceMetadataSuccess(teacherToken, rcvdResourceMetadata.getResourceId(), resource);
        rcvdResourceMetadata = resourceAPI.getResourceMetaDataSuccess(teacherToken, rcvdResourceMetadata.getResourceId());
        resourceAPI.checkSingle(resource,rcvdResourceMetadata);

        // delete resource
        resourceAPI.deleteResourceSuccess(teacherToken, rcvdResourceMetadata.getResourceId());
        assert !oss.exist(name);
    }

}
