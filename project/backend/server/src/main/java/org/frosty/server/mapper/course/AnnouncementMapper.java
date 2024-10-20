package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.frosty.server.entity.bo.Announcement;

import java.util.List;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    // 创建通知
    void createAnnouncement(@Param("courseId") Long courseId, @Param("announcement") Announcement announcement);

    // 修改通知
    void updateAnnouncement(@Param("id") Long id, @Param("announcement") Announcement announcement);

    // 删除通知
    void deleteAnnouncement(@Param("id") Long id);

    // 获取单个通知
    Announcement getAnnouncementById(@Param("id") Long id);

    // 获取某个课程下的所有通知
    List<Announcement> getAnnouncementsByCourseId(@Param("courseId") Long courseId);
}
