package org.frosty.demo.services;



import org.frosty.demo.entity.dto.Chapter;

import java.util.List;

public interface ChapterService {


    void createChapter(Chapter chapter);

    Chapter findByID(Long id);

    void updateChapter(Chapter updatedChapter);

    void deleteChapter(Long id);

    List<Chapter> getAll();
}
