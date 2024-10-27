package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.frosty.server.entity.bo.Notification;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Notification> {
    @Insert("INSERT INTO notifications (course_id, title, message) " +
            "VALUES (#{courseId}, #{title}, #{message})")
    @Options(useGeneratedKeys = true, keyProperty = "notificationId")
    void insertAnnouncement(Notification notification);
}
