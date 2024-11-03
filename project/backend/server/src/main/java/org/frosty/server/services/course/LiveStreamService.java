package org.frosty.server.services.course;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.common_service.storage.api.SharedBiMapService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LiveStreamService {
    private final SharedBiMapService sharedBiMapService;
    @PostConstruct
    public void init() {
        sharedBiMapService.init("course.live-stream");
    }
    public String getPushName(Long courseId) {
        String rndName = UUID.randomUUID().toString();
       return sharedBiMapService.getOrDefault(String.valueOf(courseId),rndName);
    }

    public String getPullName(Long courseId) {
        String res = sharedBiMapService.get(String.valueOf(courseId));
        Ex.check(res!=null, Response.getBadRequest("no-live-stream"));
        return res;
    }
}
