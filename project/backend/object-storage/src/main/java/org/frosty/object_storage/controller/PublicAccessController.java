package org.frosty.object_storage.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.object_storage.service.AccessKeyService;
import org.frosty.object_storage.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping(PathConstant.API + "/storage")
@RequiredArgsConstructor
public class PublicAccessController {
    private static final int BUF_SIZE = 1024;
    private final AccessKeyService accessKeyService;
    private final StorageService storageService;

    @GetMapping("/{objName}")
    public void getFileFromPublic(@NonNull String case_name,
                                  @NonNull String access_key,
                                  @PathVariable String objName,
                                  HttpServletResponse response) throws Exception {
        Ex.check(accessKeyService.valid(objName, case_name, access_key), Response.getUnauthorized("unauthorized"));
        try (InputStream inputStream = storageService.getFile(objName)) {
            // 设置响应的内容类型和头信息
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "inline; filename=\"" + objName + "\"");
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[BUF_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }
}
