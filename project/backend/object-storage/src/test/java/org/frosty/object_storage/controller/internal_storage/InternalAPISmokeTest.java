package org.frosty.object_storage.controller.internal_storage;

import org.frosty.object_storage.utils.ObjectStorageControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;

@ObjectStorageControllerTest
public class InternalAPISmokeTest {
    @Autowired
    private InternalStorageAPI api;
    @Test
    public void testBasicCURD() throws Exception {
        //upload file
        var file = api.getTemplateMockFileTemplate();
        api.uploadFileSuccess("test1",file);

        // check file exist
        assert api.checkFileExistSuccess("test1");

        // get file
        var resByte = api.getFileSuccess("test1");
        assert Arrays.equals(resByte, file.getBytes());

        // get accessKey
        var token = api.getAccessKeySuccess("test1", "user1");
        var token2 = api.getAccessKeySuccess("test1", "user1");
        assert token.equals(token2);

        // withdraw key
        api.withdrawAccessKey("test1", "user1");
        var token3 = api.getAccessKeySuccess("test1", "user1");
        assert !token.equals(token3); // if implementation correct, pass in most case.

        // delete file
        api.deleteFileSuccess("test1");
        assert !api.checkFileExistSuccess("test1");
    }
}
