package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.services.course.ResourceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.frosty.server.entity.po.ResourceWithAccessKey;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;
    @PostMapping("/chapter/{chapterId}/resource")
    public void uploadResource(
            @GetToken TokenInfo tokenInfo,
            @RequestPart("data") Resource resource,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        Ex.check(tokenInfo.getAuthStatus()== AuthStatus.PASS, Response.getUnauthorized("unauthorized"));
        resourceService.createResource(tokenInfo.getAuthInfo(),resource,file);
    }

    // 获取资源
    @GetMapping("resource/{id}")
    public ResourceWithAccessKey getResourceMetaData(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id) {
        Ex.check(tokenInfo.getAuthStatus()== AuthStatus.PASS, Response.getUnauthorized("unauthorized"));
        return resourceService.findById(tokenInfo.getAuthInfo(),id);
    }

    @PutMapping("resource/{id}")
    public void updateResourceMetadata(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id, @RequestBody Resource updatedResource) {
        Ex.idCheck(id,updatedResource.getResourceId());
        Ex.check(tokenInfo.getAuthStatus()== AuthStatus.PASS, Response.getUnauthorized("unauthorized"));
        resourceService.updateResource(tokenInfo.getAuthInfo(),updatedResource);
    }

    @DeleteMapping("resource/{id}")
    public void deleteResource(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id) {
        Ex.check(tokenInfo.getAuthStatus()== AuthStatus.PASS, Response.getUnauthorized("unauthorized"));
        resourceService.deleteResource(tokenInfo.getAuthInfo(),id);
    }

    @GetMapping("/chapter/{chapterId}/resource")
    public Map<String,List<ResourceWithAccessKey>> getResourcesByChapter(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long chapterId) {
        Ex.check(tokenInfo.getAuthStatus()== AuthStatus.PASS, Response.getUnauthorized("unauthorized"));
        return Map.of("content",resourceService.getResourcesByChapter(tokenInfo.getAuthInfo(),chapterId));
    }
}
