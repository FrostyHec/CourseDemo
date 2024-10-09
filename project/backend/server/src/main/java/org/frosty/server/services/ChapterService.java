package org.frosty.server.services;


import org.frosty.server.entity.bo.Chapter;

import java.util.List;

public interface ChapterService {


    void createChapter(Chapter chapter);

    Chapter findByID(Long id);

    void updateChapter(Chapter updatedChapter);

    void deleteChapter(Long id);

    List<Chapter> getAll();
}
