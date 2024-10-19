package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.Announcement;

import org.frosty.server.services.course.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // 创建公告
    @PostMapping("/course/{id}/announcement")
    public Response createAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        announcementService.createAnnouncement(id, announcement);
        return Response.getSuccess("Announcement created successfully");
    }

    // 修改公告
    @PutMapping("/announcement/{id}")
    public Response updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        announcementService.updateAnnouncement(id, announcement);
        return Response.getSuccess("Announcement updated successfully");
    }

    // 删除公告
    @DeleteMapping("/announcement/{id}")
    public Response deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return Response.getSuccess("Announcement deleted successfully");
    }

    // 获取公告详情
    @GetMapping("/announcement/{id}")
    public Response getAnnouncementById(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        return Response.getSuccess(announcement);
    }

    // 获取课程下的公告
    @GetMapping("/course/{id}/announcement")
    public Response getAnnouncementsByCourseId(@PathVariable Long id) {
        List<Announcement> announcements = announcementService.getAnnouncementsByCourseId(id);
        return Response.getSuccess(announcements);
    }

    // 通知站内推送
    @PostMapping("/announcement/{id}/site-notify")
    public Response notifyViaSite(@PathVariable Long id) {
        announcementService.notifyViaSite(id);
        return Response.getSuccess("Site notification sent successfully");
    }

    // 通知邮件推送
    @PostMapping("/announcement/{id}/email-notify")
    public Response notifyViaEmail(@PathVariable Long id) {
        announcementService.notifyViaEmail(id);
        return Response.getSuccess("Email notification sent successfully");
    }
}
