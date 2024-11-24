package org.frosty.server.services.course;


import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.entity.po.ResourceWithAccessKey;
import org.frosty.server.mapper.course.ResourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceMapper resourceMapper;
    private final ObjectStorageService objectStorageService;

    @Transactional
    public void createResource(AuthInfo tokenInfo, Resource resource, MultipartFile file) throws IOException {
        // TODO check called by teacher & course owner
        String fileName = UUID.randomUUID().toString() + file.getSize() + "." + resource.getSuffix();
        fileName = fileName.replace("/","."); // TODO 防止注入漏洞
        resource.setFileName(fileName);

        resourceMapper.insert(resource);
        objectStorageService.save(resource.getFileName(), file.getBytes());
    }

    public ResourceWithAccessKey findById(AuthInfo tokenInfo, Long id) {
        // TODO check called by
        //          owner teacher -> all
        //          invited student -> reject if chapter invisible
        //          public student -> reject if chapter invisible or resource non-public
        var e = resourceMapper.selectById(id);
        return getResourceWithAccessKey(tokenInfo, e);
    }

    private ResourceWithAccessKey getResourceWithAccessKey(AuthInfo authInfo, Resource resource) {
        var accessKey = objectStorageService.getAccessKey(resource.getFileName(),
                getCaseName(authInfo.getUserID()));
        return new ResourceWithAccessKey(resource, accessKey);
    }


    public void updateResource(AuthInfo tokenInfo, Resource updatedResource) {
        // TODO check called by teacher & course owner
        resourceMapper.updateById(updatedResource);
    }

    @Transactional
    public void deleteResource(AuthInfo tokenInfo, Long id) {
        // TODO check called by teacher & course owner
        var resource = resourceMapper.selectById(id);
        var name = resource.getFileName();
        resourceMapper.deleteById(id);
        objectStorageService.delete(name);
    }


    public List<ResourceWithAccessKey> getResourcesByChapter(AuthInfo authInfo, Long chapterId) {
        // TODO check called by
        //          owner teacher -> all
        //          invited student -> filter if chapter invisible
        //          public student -> filter if chapter invisible or resource non-public
        var resources = resourceMapper.getAll(chapterId);
        List<ResourceWithAccessKey> resourceWithAccessKeys = new ArrayList<>(resources.size());
        for (Resource resource : resources) {
            resourceWithAccessKeys.add(getResourceWithAccessKey(authInfo, resource));
        }
        return resourceWithAccessKeys;
    }

    private String getCaseName(Long uid) {
        return "resource-" + uid;
    }

}
