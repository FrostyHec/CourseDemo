package org.frosty.server.controller.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Notification;
import org.frosty.server.entity.po.NotificationWithReceiver;
import org.frosty.server.services.course.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 创建公告
    @PostMapping("/course/{id}/announcement")
    public void createAnnouncement(@PathVariable Long id, @RequestBody NotificationWithReceiver notification) {
        notificationService.createNotification(id, notification);
    }

    // 修改公告
    @PutMapping("/announcement/{id}")
    public void updateAnnouncement(@PathVariable Long id, @RequestBody NotificationWithReceiver notification) {
        notificationService.updateNotification(id, notification);
    }

    // 删除公告
    @DeleteMapping("/announcement/{id}")
    public void deleteAnnouncement(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

    // 获取公告详情
    @GetMapping("/announcement/{id}")
    public Notification getAnnouncementById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    // 获取课程下的公告
    @GetMapping("/course/{id}/announcement")
    public NotificationList getAnnouncementsByCourseId(@PathVariable Long id) {
        List<Notification> notifications = notificationService.getNotificationsByCourseId(id);
        return new NotificationList(notifications);
    }

    // 通知站内推送
    @PostMapping("/announcement/{id}/site-notify")
    public void notifyViaSite(@PathVariable Long id) {
        notificationService.notifyViaSite(id);
    }

    // 通知邮件推送
    @PostMapping("/announcement/{id}/email-notify")
    public void notifyViaEmail(@PathVariable Long id) {
        notificationService.notifyViaEmail(id);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class NotificationList {
        private List<Notification> content;
    }
}
