package org.frosty.server.services.course;


import org.frosty.server.entity.bo.Chapter;

import java.util.List;

public interface ChapterService {


    void createChapter(Chapter chapter);

    Chapter findByID(Long id);

    void updateChapter(Long id, Chapter updatedChapter);

    void deleteChapter(Long id);

    List<Chapter> getAllChaptersByCourseId(Long courseId);
//List<Chapter> getAll();
}
