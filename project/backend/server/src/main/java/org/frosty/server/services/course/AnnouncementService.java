package org.frosty.server.services.course;

import org.frosty.server.entity.bo.Announcement;

import java.util.List;

public interface AnnouncementService {

    // 创建公告
    void createAnnouncement(Long courseId, Announcement announcement);

    // 修改公告
    void updateAnnouncement(Long id, Announcement announcement);

    // 删除公告
    void deleteAnnouncement(Long id);

    // 获取公告详情
    Announcement getAnnouncementById(Long id);

    // 获取课程下所有公告
    List<Announcement> getAnnouncementsByCourseId(Long courseId);

    // 通知站内推送
    void notifyViaSite(Long announcementId);

    // 通知邮件推送
    void notifyViaEmail(Long announcementId);
}
