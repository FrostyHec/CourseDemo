package org.frosty.server.services.course.impl;


import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.mapper.course.ChapterMapper;
import org.frosty.server.services.course.ChapterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterMapper chapterMapper;

    @Override
    public void createChapter(Chapter chapter) {
        chapterMapper.insertChapter(chapter);
    }

    @Override
    public Chapter findByID(Long id) {
        return chapterMapper.getChapterById(id);
    }

    @Override
    public void updateChapter(Long id, Chapter updatedChapter) {
        chapterMapper.updateChapter(id, updatedChapter.getCourseId(), updatedChapter.getChapterTitle(), updatedChapter.getChapterType(), updatedChapter.getContent());
    }

    @Override
    public void deleteChapter(Long id) {
        chapterMapper.deleteChapterById(id);
    }

    @Override
    public List<Chapter> getAllChaptersByCourseId(Long courseId) {
        List<Chapter> chapters = chapterMapper.getAllChaptersByCourseId(courseId);
        return chapters;
    }
}
