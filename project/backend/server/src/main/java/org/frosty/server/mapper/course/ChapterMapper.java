package org.frosty.server.mapper.course;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.frosty.server.entity.bo.Chapter;


import java.util.List;

// 看看mybatis_plus
@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {

    // 插入一个新章节
    @Insert("INSERT INTO chapters(course_id, chapter_title,chapter_type, content) VALUES(#{courseId}, #{chapterTitle}, #{chapterType}, #{content})")
    void insertChapter(Chapter chapter);

    // 根据 ID 更新章节
    // ignore this error
    @Update("UPDATE chapters SET course_id=#{courseId}, chapter_title=#{chapterTitle}, chapter_type=#{chapterType}, content=#{content} WHERE chapter_id=#{id}")
    void updateChapter(@Param("id")Long id, @Param("courseId")Long courseId, @Param("chapterTitle")String chapterTitle, @Param("chapterType")Chapter.ChapterType chapterType, @Param("content")String content);

    // 根据 ID 获取章节
    @Select("SELECT * FROM chapters WHERE chapters.chapter_id=#{id}")
    Chapter getChapterById(@Param("id") Long id);

    // 根据 ID 删除章节
    @Delete("DELETE FROM chapters WHERE chapters.chapter_id=#{id}")
    void deleteChapterById(@Param("id") Long id);

    // 获取某个课程的全部章节
    @Select("SELECT * FROM chapters WHERE course_id=#{courseId}")
    List<Chapter> getAllChaptersByCourseId(@Param("courseId") Long courseId);

    @Select("SELECT * FROM chapters")
    List<Chapter> getAll();


}
