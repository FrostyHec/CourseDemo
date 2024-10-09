package org.frosty.server.services.impl;


import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.mapper.ChapterMapper;
import org.frosty.server.services.ChapterService;
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
