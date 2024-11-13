package org.frosty.server.services.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.RequiredArgsConstructor;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.common_service.im.entity.Email;
import org.frosty.common_service.im.entity.SiteMessage;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.Notification;
import org.frosty.server.entity.bo.NotificationReceiver;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.NotificationWithReceiver;
import org.frosty.server.mapper.course.CourseMapper;
import org.frosty.server.mapper.course.NotificationMapper;
import org.frosty.server.mapper.course.NotificationReceiverMapper;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationReceiverMapper notificationReceiverMapper;
    private final MessagePushService messagePushService;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    // 创建公告
    public void createNotification(Long courseId, NotificationWithReceiver notificationWithReceiver) {
        Notification notification = new Notification();
        notification.setCourseId(courseId);
        notification.setTitle(notificationWithReceiver.getTitle());
        notification.setMessage(notificationWithReceiver.getMessage());
        notificationMapper.insertNotification(notification);

        if (notificationWithReceiver.getReceiverIds() == null || notificationWithReceiver.getReceiverIds().isEmpty()) {
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
        if (notificationWithReceiver.getReceiverIds() == null || notificationWithReceiver.getReceiverIds().isEmpty()) {
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
        // 获取所有接收者
        QueryWrapper<NotificationReceiver> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notification_id", id);
        List<NotificationReceiver> notificationReceivers = notificationReceiverMapper.selectList(queryWrapper);
        List<Long> receiverIds = notificationReceivers.stream()
                .map(NotificationReceiver::getReceiverId)
                .toList();

        // 获取公告信息
        QueryWrapper<Notification> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("notification_id", id);
        Notification notification = notificationMapper.selectOne(queryWrapper1);
        JsonNode message = new TextNode(notification.getTitle() + "\n" + notification.getMessage());

        // 获取教师id（发送者id）
        QueryWrapper<Course> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("teacher_id", notification.getCourseId());
        Course course = courseMapper.selectOne(queryWrapper2);

        // 设置消息格式
        SiteMessage siteMessage = new SiteMessage();
        siteMessage.setBody(message);
        siteMessage.setFromId(course.getTeacherId());
        siteMessage.setType(SiteMessage.MessageType.NEW);
        siteMessage.setRequiredAck(false); // not sure

        // todo：并行化以下部分
        for (Long receiverId : receiverIds) {
            siteMessage.setToId(receiverId);
            messagePushService.pushSite(siteMessage);
        }
    }

    // 通知邮件推送
    public void notifyViaEmail(Long id) {
        // 获取所有接收者
        QueryWrapper<NotificationReceiver> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notification_id", id);
        List<NotificationReceiver> notificationReceivers = notificationReceiverMapper.selectList(queryWrapper);
        List<Long> receiverIds = notificationReceivers.stream()
                .map(NotificationReceiver::getReceiverId)
                .toList();

        // 获取接收者邮箱
        QueryWrapper<User> emailQueryWrapper = new QueryWrapper<>();
        emailQueryWrapper.in("user_id", receiverIds);
        List<User> users = userMapper.selectList(emailQueryWrapper);
        List<String> emails = users.stream()
                .map(User::getEmail)
                .toList();

        // 获取公告信息
        QueryWrapper<Notification> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("notification_id", id);
        Notification notification = notificationMapper.selectOne(queryWrapper1);
        if (notification == null) {
            return;
        }

        // 获取教师邮箱（发送者邮箱）
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("user_id", notification.getCourseId());
        User teacher = userMapper.selectOne(queryWrapper2);
        if (teacher == null) {
            return;
        }
        String fromEmail = teacher.getEmail();

        Email email = new Email();
        email.setSubject(notification.getTitle());
        email.setText(notification.getMessage());
        email.setFromEmail(fromEmail);
        email.setFromName(teacher.getFirstName() + " " + teacher.getLastName());
        // todo：并行化以下部分
        for (String toEmail : emails) {
            email.setToEmail(toEmail);
            messagePushService.pushEmail(email);
        }
    }
}
