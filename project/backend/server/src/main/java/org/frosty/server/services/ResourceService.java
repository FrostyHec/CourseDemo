package org.frosty.demo.services;


import org.frosty.demo.entity.dto.Resource;

import java.util.List;

public interface ResourceService {
    void createResource(Resource resource);

    Resource findById(Long id);

    void updateResource(Resource updatedResource);

    void deleteResource(Long id);

    List<Resource> getResourcesByChapter(Long chapterId);
}
