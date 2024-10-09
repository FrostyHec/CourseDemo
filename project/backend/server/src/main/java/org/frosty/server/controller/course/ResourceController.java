package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.services.course.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API + "/chapter/{chapterId}/resource")
@RequiredArgsConstructor
// 先这样写着做一个占位，具体三种chapter，以及其所有的不同resource具体内容再具体分析
public class ResourceController {

    private final ResourceService resourceService;

    // 上传课程资源
    @PostMapping
    public Map<String, String> uploadResource(@RequestBody Resource resource) {
        resourceService.createResource(resource);
        return Map.of("message", "Successfully uploaded resource.");
    }

    // 获取资源
    @GetMapping("/{id}")
    public Resource getResource(@PathVariable Long id) {
        return resourceService.findById(id);
    }

    // 更新课程资源
    @PutMapping("/{id}")
    public Map<String, String> updateResource(@PathVariable Long id, @RequestBody Resource updatedResource) {
        resourceService.updateResource(updatedResource);
        return Map.of("message", "Successfully updated resource.");
    }

    // 删除资源
    @DeleteMapping("/{id}")
    public Map<String, String> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return Map.of("message", "Successfully deleted resource.");
    }

    // 获取章节下的所有资源
    @GetMapping
    public List<Resource> getResourcesByChapter(@PathVariable Long chapterId) {
        return resourceService.getResourcesByChapter(chapterId);
    }
}
