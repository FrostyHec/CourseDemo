package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.frosty.server.entity.bo.Resource;

import java.util.List;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
    //    // @Insert("INSERT INTO chapters(course_id, chapter_title,chapter_type, content) VALUES(#{courseId}, #{chapter_title}, #{chapter_type}, #{content})")
//    void insertResource(Resource resource);
//
//    @Select("SELECT * FROM resources WHERE resources.resource_id=#{id}")
//    Resource getResourceById(Long id);
//
//    // @Update("UPDATE resources SET course_id =#{course_id},chapter_title=#{chapter_title},chapter_type=#{chapter_type}, content=#{content} WHERE chapters.chapter_id=#{id}")
//    void updateResource(Resource updatedResource);
//
//    @Delete("DELETE FROM resources WHERE resources.resource_id=#{id}")
//    void deleteResource(Long id);
//
    @Select("SELECT * FROM resources where chapter_id=#{chapterId}")
    List<Resource> getAll(Long chapterId);
}
