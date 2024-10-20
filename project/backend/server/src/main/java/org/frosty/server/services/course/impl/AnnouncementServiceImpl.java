package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.Announcement;
import org.frosty.server.mapper.course.AnnouncementMapper;
import org.frosty.server.services.course.AnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Override
    @Transactional
    public void createAnnouncement(Long courseId, Announcement announcement) {
        announcement.setCourseId(courseId);
        announcementMapper.createAnnouncement(courseId, announcement);
    }

    @Override
    @Transactional
    public void updateAnnouncement(Long id, Announcement announcement) {
        announcementMapper.updateAnnouncement(id, announcement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementMapper.deleteAnnouncement(id);
    }

    @Override
    public Announcement getAnnouncementById(Long id) {
        return announcementMapper.getAnnouncementById(id);
    }

    @Override
    public List<Announcement> getAnnouncementsByCourseId(Long courseId) {
        return announcementMapper.getAnnouncementsByCourseId(courseId);
    }

    @Override
    public void notifyViaSite(Long announcementId) {
        // 站内推送通知的具体实现
        Announcement announcement = announcementMapper.getAnnouncementById(announcementId);
        // 逻辑处理
    }

    @Override
    public void notifyViaEmail(Long announcementId) {
        // 邮件推送通知的具体实现
        Announcement announcement = announcementMapper.getAnnouncementById(announcementId);
        // 逻辑处理
    }
}
