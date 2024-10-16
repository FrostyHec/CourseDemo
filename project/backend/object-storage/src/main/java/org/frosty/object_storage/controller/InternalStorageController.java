package org.frosty.object_storage.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.annotations.Delete;
import org.frosty.common.constant.PathConstant;
import org.frosty.object_storage.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.INTERNAL_API+"/storage")
@RequiredArgsConstructor
public class InternalStorageController {
    private final StorageService storageService;
    private static final int BUF_SIZE = 1024;
    @PostMapping("/{key}")
    public void uploadFile(@PathVariable String key, HttpServletRequest request) throws Exception {
        InputStream inputStream = request.getInputStream();
        String contentType = request.getContentType();
        storageService.uploadFile(key, inputStream,contentType);
    }

    @GetMapping("/{key}")
    public void getFile(@PathVariable String key, HttpServletResponse response) throws Exception {
        try (InputStream fileStream = storageService.getFile(key);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            byte[] buffer = new byte[BUF_SIZE];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }

    @DeleteMapping("/{key}")
    public void deleteFile(@PathVariable String key) throws Exception {
        storageService.deleteFile(key);
    }

    @GetMapping("/{key}/exists")
    public Map<String,Object> checkFileExist(@PathVariable String key) throws Exception {
        boolean exists = storageService.checkFileExist(key);
        return Map.of("exists", exists);
    }

    @GetMapping("/{key}/access-key")
    public Map<String,Object> getAccessKey(@PathVariable String key,@NonNull String case_name) {
        String accessKey = storageService.getAccessKey(key,case_name);
        return Map.of("access_key", accessKey);
    }

    @DeleteMapping("/{key}/access-key")
    public void withdrawAccessKey(@PathVariable String key,@NonNull String case_name) {
        storageService.deleteAccessKey(key,case_name);
    }
}
