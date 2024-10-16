package org.frosty.object_storage.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.object_storage.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/{key}")
    public void uploadFile(@PathVariable String key, @RequestPart("file") MultipartFile file) throws Exception {
        storageService.uploadFile(key, file);
    }

    @GetMapping("/{key}")
    public void getFile(@PathVariable String key, HttpServletResponse response) throws Exception {
//        InputStream fileStream = storageService.getFile(key);
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(fileStream);
        try (InputStream fileStream = storageService.getFile(key);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            byte[] buffer = new byte[1024];
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
}
