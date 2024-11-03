package org.frosty.server.services.course;

import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.Resource;

import java.util.List;

public interface CourseComplexQueryService {
    Course findCourseById(Long id);

    List<Chapter> findChapterByCourseId(Long id);

    List<Resource> findResourceByChapterId(Long chapterId);

    List<Assignment> findAssByChapterId(Long chapterId);
}
