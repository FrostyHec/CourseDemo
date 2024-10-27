package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.announcement.AnnouncementAPI;
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
public class AnnouncementSmokeTest {

    @Autowired
    private AuthAPI authAPI;

    @Autowired
    private CourseAPI courseAPI;

    @Autowired
    private AnnouncementAPI announcementAPI;

    @Test
    public void testAnnouncementCRUD() throws Exception {
        // Teacher login
        var pair = authAPI.quickAddUserAndLogin("teacher", User.Role.teacher);
        var teacherToken = pair.first;
        var teacher = pair.second;

        // Create a course
        var course = courseAPI.getTemplatePublishedCourse(
                teacher.getUserId(), "testCourse", Course.PublicationType.open);
        courseAPI.quickCreateCourse(course);

        // Student login
        pair = authAPI.quickAddUserAndLogin("student", User.Role.student);
        var studentToken = pair.first;
        var student = pair.second;

        // Teacher creates an announcement
        var announcement = new Announcement();
        announcement.setMessage("This is a test announcement.");
        announcement.setTitle("Test Announcement");
        announcement.setCourseId(course.getCourseId());
        announcement.setReceiverIds(List.of(student.getUserId().intValue()));
        announcementAPI.createAnnouncementSuccess(teacherToken, course.getCourseId(), announcement);

        // Student receives the announcement
        var announcements = announcementAPI.getAnnouncementsForCourseSuccess(studentToken, course.getCourseId());
        var receivedAnnouncement = CommonCheck.checkSingleAndGet(announcements);
        assertEquals(announcement.getMessage(), receivedAnnouncement.getMessage());

        // Teacher updates the announcement
        announcement.setMessage("This is an updated test announcement.");
        announcement.setTitle("Updated Test Announcement");
        announcementAPI.updateAnnouncementSuccess(teacherToken, receivedAnnouncement.getNotificationId(), announcement);

        // Student receives the updated announcement
        announcements = announcementAPI.getAnnouncementsForCourseSuccess(studentToken, course.getCourseId());
        receivedAnnouncement = CommonCheck.checkSingleAndGet(announcements);
        assertEquals(announcement.getMessage(), receivedAnnouncement.getMessage());
        assertEquals(announcement.getTitle(), receivedAnnouncement.getTitle());

        // Teacher deletes the announcement
        announcementAPI.deleteAnnouncementSuccess(teacherToken, receivedAnnouncement.getNotificationId());

        // Student can't find the announcement
        announcements = announcementAPI.getAnnouncementsForCourseSuccess(studentToken, course.getCourseId());
        assertTrue(announcements.isEmpty());
    }
}