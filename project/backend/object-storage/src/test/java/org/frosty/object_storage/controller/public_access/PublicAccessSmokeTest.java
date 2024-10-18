package org.frosty.object_storage.controller.public_access;

import org.frosty.object_storage.controller.internal_storage.InternalStorageAPI;
import org.frosty.object_storage.utils.ObjectStorageControllerTest;
import org.frosty.test_common.utils.RespChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@ObjectStorageControllerTest
public class PublicAccessSmokeTest {
    @Autowired
    private InternalStorageAPI internalAPI;
    @Autowired
    private PublicAccessAPI api;

    @Test
    public void testBasicAccessFlow() throws Exception {
        String key="test1";
        String caseName="user1";
        var file = internalAPI.getTemplateMockFileTemplate();
        internalAPI.uploadFileSuccess(key,file);

        api.getFileFromPublic(caseName,"",key)
                .andExpect(RespChecker.unauthorized());

        var token = internalAPI.getAccessKeySuccess(key,caseName);
        var resByte = api.getFileFromPublicSuccess(caseName,token,key);
        assert Arrays.equals(resByte, file.getBytes());

        internalAPI.withdrawAccessKey(key,caseName);
        api.getFileFromPublic(caseName,token,key)
                .andExpect(RespChecker.unauthorized());
    }
}
