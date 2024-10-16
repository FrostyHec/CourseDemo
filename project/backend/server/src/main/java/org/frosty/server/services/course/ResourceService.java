package org.frosty.server.services.course;


import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.mapper.course.ResourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceService  {
    private final ResourceMapper resourceMapper;
    private final ObjectStorageService objectStorageService;
    public void createResource(TokenInfo tokenInfo,Resource resource, MultipartFile file) throws IOException {
        // TODO check called by teacher & course owner
        resource.setFileName(UUID.randomUUID().toString()+file.getSize()+"."+resource.getSuffix());
        resourceMapper.insert(resource);
        objectStorageService.save(resource.getFileName(),file.getBytes());
    }

    public Resource findById(TokenInfo tokenInfo,Long id) {
        // TODO check called by
        //          owner teacher -> all
        //          invited student -> reject if chapter invisible
        //          public student -> reject if chapter invisible or resource non-public
        return resourceMapper.selectById(id);
    }


    public void updateResource(TokenInfo tokenInfo,Resource updatedResource) {
        // TODO check called by teacher & course owner
        resourceMapper.updateById(updatedResource);
    }

    @Transactional
    public void deleteResource(TokenInfo tokenInfo,Long id) {
        // TODO check called by teacher & course owner
        var resource = resourceMapper.selectById(id);
        var name = resource.getFileName();
        resourceMapper.deleteById(id);
        objectStorageService.delete(name);
    }


    public List<Resource> getResourcesByChapter(TokenInfo tokenInfo,Long chapterId) {
        // TODO check called by
        //          owner teacher -> all
        //          invited student -> filter if chapter invisible
        //          public student -> filter if chapter invisible or resource non-public
        return resourceMapper.getAll(chapterId);
    }
}
