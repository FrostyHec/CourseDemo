package org.frosty.demo.mapper;


import org.apache.ibatis.annotations.*;
import org.frosty.demo.entity.dto.Chapter;


import java.util.List;

// 看看mybatis_plus
@Mapper
public interface ChapterMapper {

    // 插入一个新章节
    @Insert("INSERT INTO chapters(course_id, chapter_title,chapter_type, content) VALUES(#{courseId}, #{chapter_title}, #{chapter_type}, #{content})")
    void insertChapter(Chapter chapter);

    // 根据 ID 更新章节
    // 这里IDEA一直在"="号前面报错，但是我在ass1中使用的时候并不影响，不知道为什么
    @Update("UPDATE chapters SET course_id =#{course_id},chapter_title=#{chapter_title},chapter_type=#{chapter_type}, content=#{content} WHERE chapters.chapter_id=#{id}")
    void updateChapter(Chapter chapter);

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
