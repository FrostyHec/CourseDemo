package org.frosty.server.services.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.mapper.course.AnnouncementMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementMapper announcementMapper;

    // 创建公告
    public void createAnnouncement(Long courseId, Announcement announcement) {
        announcement.setCourseId(courseId);
        announcementMapper.insertAnnouncement(announcement);

    }

    // 修改公告
    public void updateAnnouncement(Long id, Announcement announcement) {
        announcement.setNotificationId(id);
        announcementMapper.updateById(announcement);
    }

    // 删除公告
    public void deleteAnnouncement(Long id) {
        announcementMapper.deleteById(id);
    }

    // 获取公告详情
    public Announcement getAnnouncementById(Long id) {
        return announcementMapper.selectById(id);
    }

    // 获取课程下所有公告
    public List<Announcement> getAnnouncementsByCourseId(Long courseId) {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);  // 这里根据具体的字段名指定查询条件

        return announcementMapper.selectList(queryWrapper);  // 使用BaseMapper的selectList方法
    }

    // 通知站内推送
    public void notifyViaSite(Long announcementId) {
        // todo
    }

    // 通知邮件推送
    public void notifyViaEmail(Long announcementId) {
        // todo
    }
}
