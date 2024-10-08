package org.frosty.demo.services.impl;



import org.frosty.demo.entity.dto.Chapter;
import org.frosty.demo.mapper.ChapterMapper;
import org.frosty.demo.services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;


    @Override
    public void createChapter(Chapter chapter) {
        chapterMapper.insertChapter(chapter);
    }

    @Override
    public Chapter findByID(Long id) {
        return chapterMapper.getChapterById(id);
    }

    @Override
    public void updateChapter(Chapter updatedChapter) {
        chapterMapper.updateChapter(updatedChapter);
    }

    @Override
    public void deleteChapter(Long id) {
        chapterMapper.deleteChapterById(id);
    }

    @Override
    public List<Chapter> getAll() {
        List<Chapter> chapters = chapterMapper.getAll();
        return chapters;
    }
}
