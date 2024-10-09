package org.frosty.server.services.course.impl;

import org.frosty.server.entity.bo.Resource;
import org.frosty.server.mapper.course.ResourceMapper;
import org.frosty.server.services.course.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl  implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public void createResource(Resource resource) {
        resourceMapper.insertResource(resource);
    }

    @Override
    public Resource findById(Long id) {
        return resourceMapper.getResourceById(id);
    }

    @Override
    public void updateResource(Resource updatedResource) {
        resourceMapper.updateResource(updatedResource);
    }

    @Override
    public void deleteResource(Long id) {
        resourceMapper.deleteResource(id);
    }

    @Override
    public List<Resource> getResourcesByChapter(Long chapterId) {
        List<Resource> resources = resourceMapper.getAll();
        return resources;
    }
}
