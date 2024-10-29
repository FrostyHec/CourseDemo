package org.frosty.server.services.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.Notification;
import org.frosty.server.entity.bo.NotificationReceiver;
import org.frosty.server.entity.po.NotificationWithReceiver;
import org.frosty.server.mapper.course.NotificationMapper;
import org.frosty.server.mapper.course.NotificationReceiverMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationReceiverMapper notificationReceiverMapper;

    // 创建公告
    public void createNotification(Long courseId, NotificationWithReceiver notificationWithReceiver) {
        Notification notification = new Notification();
        notification.setCourseId(courseId);
        notification.setTitle(notificationWithReceiver.getTitle());
        notification.setMessage(notificationWithReceiver.getMessage());
        notificationMapper.insertNotification(notification);

        if(notificationWithReceiver.getReceiverIds() == null || notificationWithReceiver.getReceiverIds().isEmpty()) {
            return;
        }
        List<NotificationReceiver> notificationReceivers = notificationWithReceiver.getReceiverIds().stream()
                .map(receiverId -> new NotificationReceiver(notification.getNotificationId(), receiverId))
                .toList();
        notificationReceiverMapper.insert(notificationReceivers);
    }

    // 修改公告
    public void updateNotification(Long id, NotificationWithReceiver notificationWithReceiver) {
        Notification notification = new Notification();
        notification.setNotificationId(id);
        notification.setTitle(notificationWithReceiver.getTitle());
        notification.setMessage(notificationWithReceiver.getMessage());
        notificationMapper.updateById(notification);

        // update notification receivers
        if(notificationWithReceiver.getReceiverIds() == null || notificationWithReceiver.getReceiverIds().isEmpty()) {
            return;
        }
        List<NotificationReceiver> notificationReceivers = notificationWithReceiver.getReceiverIds().stream()
                .map(receiverId -> new NotificationReceiver(notification.getNotificationId(), receiverId))
                .toList();
        QueryWrapper<NotificationReceiver> deleteQueryWrapper = new QueryWrapper<>();
        deleteQueryWrapper.eq("notification_id", id);
        notificationReceiverMapper.delete(deleteQueryWrapper);
        notificationReceiverMapper.insert(notificationReceivers);
    }

    // 删除公告
    public void deleteNotification(Long id) {
        notificationMapper.deleteById(id);
    }

    // 获取公告详情
    public Notification getNotificationById(Long id) {
        return notificationMapper.selectById(id);
    }

    // 获取课程下所有公告
    public List<Notification> getNotificationsByCourseId(Long courseId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);  // 这里根据具体的字段名指定查询条件

        return notificationMapper.selectList(queryWrapper);  // 使用BaseMapper的selectList方法
    }

    // 通知站内推送
    public void notifyViaSite(Long id) {
        // todo
    }

    // 通知邮件推送
    public void notifyViaEmail(Long id) {
        // todo
    }
}
