package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.*;
import org.frosty.server.mapper.course.AssignmentMapper;
import org.frosty.server.mapper.course.ChapterMapper;
import org.frosty.server.mapper.course.CourseMapper;
import org.frosty.server.mapper.course.ResourceMapper;
import org.frosty.server.services.course.CourseComplexQueryService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseComplexQueryServiceImpl implements CourseComplexQueryService {
    private final CourseMapper courseMapper;
    private final ChapterMapper chapterMapper;
    private final AssignmentMapper assignmentMapper;
    private final ResourceMapper resourceMapper;


    @Override
    public Course findCourseById(Long id) {
        return courseMapper.selectById(id);
    }

    @Override
    public List<Chapter> findChapterByCourseId(Long id) {
        return chapterMapper.getAllChaptersByCourseId(id);
    }

    @Override
    public List<Resource> findContentByChapterId(Long chapterId) {
        return resourceMapper.getAll(chapterId);
    }

    @Override
    public List<Assignment> findAssByChapterId(Long chapterId) {
        return assignmentMapper.getAllByChapterId(chapterId);
    }
}
