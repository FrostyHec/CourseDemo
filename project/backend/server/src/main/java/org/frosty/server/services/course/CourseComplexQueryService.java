package org.frosty.server.services.course;

import org.frosty.server.entity.bo.*;

import java.util.List;

public interface CourseComplexQueryService {
    Course findCourseById(Long id);

    List<Chapter> findChapterByCourseId(Long id);

    List<Resource> findContentByChapterId(Long chapterId);

    List<Assignment> findAssByChapterId(Long chapterId);
}
