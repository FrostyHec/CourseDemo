package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.Notification;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.NotificationWithReceiver;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.notification.NotificationAPI;
import org.frosty.server.test.controller.course.course.CourseAPI;
import org.frosty.server.test.tools.CommonCheck;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@IdempotentControllerTest
public class NotificationSmokeTest {

    @Autowired
    private AuthAPI authAPI;

    @Autowired
    private CourseAPI courseAPI;

    @Autowired
    private NotificationAPI notificationAPI;

    @Test
    public void testAnnouncementCRUD() throws Exception {
        // Teacher login
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        // Create a course
        var course = courseAPI.getTemplatePublishedCourse(teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Student login
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        // Teacher creates a Notification
        var notification = new NotificationWithReceiver();
        notification.setMessage("This is a test Notification.");
        notification.setTitle("Test Notification");
        notification.setCourseId(course.getCourseId());
        notification.setReceiverIds(List.of(student.getUserId()));
        notificationAPI.createAnnouncementSuccess(teacherToken, course.getCourseId(), notification);

        // Student receives the Notification
        var Notifications = notificationAPI.getAnnouncementsForCourseSuccess(studentToken, course.getCourseId());
        var receivedAnnouncement = CommonCheck.checkSingleAndGet(Notifications);
        assertEquals(notification.getMessage(), receivedAnnouncement.getMessage());

        // Teacher updates the Notification
        notification.setReceiverIds(List.of(student.getUserId(), teacher.getUserId()));
        notification.setMessage("This is an updated test Notification.");
        notification.setTitle("Updated Test Notification");
        notificationAPI.updateAnnouncementSuccess(teacherToken, receivedAnnouncement.getNotificationId(), notification);

        // Student receives the updated Notification
        Notifications = notificationAPI.getAnnouncementsForCourseSuccess(studentToken, course.getCourseId());
        receivedAnnouncement = CommonCheck.checkSingleAndGet(Notifications);
        assertEquals(notification.getMessage(), receivedAnnouncement.getMessage());
        assertEquals(notification.getTitle(), receivedAnnouncement.getTitle());

        // Teacher deletes the Notification
        notificationAPI.deleteAnnouncementSuccess(teacherToken, receivedAnnouncement.getNotificationId());

        // Student can't find the Notification
        Notifications = notificationAPI.getAnnouncementsForCourseSuccess(studentToken, course.getCourseId());
        assertTrue(Notifications.isEmpty());
    }
}