package org.frosty.server.services.course.progress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.controller.course.progress.CourseProgressController;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.entity.bo.progress.ChapterCompleteRecord;
import org.frosty.server.entity.bo.progress.CourseCompleteRecord;
import org.frosty.server.entity.bo.progress.ResourceCompleteRecord;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.mapper.course.ChapterMapper;
import org.frosty.server.mapper.course.ResourceMapper;
import org.frosty.server.mapper.course.cheat_check.VideoRequiredSecondsMapper;
import org.frosty.server.mapper.course.cheat_check.VideoWatchedRecordMapper;
import org.frosty.server.mapper.course.progress.ChapterCompleteMapper;
import org.frosty.server.mapper.course.progress.CourseCompleteMapper;
import org.frosty.server.mapper.course.progress.ResourceCompleteMapper;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseProgressService {
    private final UserMapper userMapper;
    private final ResourceCompleteMapper resourceCompleteMapper;
    private final ChapterCompleteMapper chapterCompleteMapper;
    private final CourseCompleteMapper courseCompleteMapper;
    // resource check mappers
    private final ResourceMapper resourceMapper;
    private final ChapterMapper chapterMapper;
    private final VideoRequiredSecondsMapper videoRequiredSecondsMapper;
    private final VideoWatchedRecordMapper videoWatchedRecordMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void completeResource(Long rid, Long uid) {
        // if has min required play time check if complete&remove metadata
        // if no, simply complete
        // only complete video resource
        if(resourceCompleteMapper.contains(rid,uid)){
            return;
        }
        var resource = resourceMapper.selectById(rid);
        Ex.check(resource.getResourceType() == Resource.ResourceType.video,
                Response.getBadRequest("cannot-complete-nonvideo"));
        checkStudentCompleteResource(rid, uid);
        // add complete
        if(!resourceCompleteMapper.contains(rid,uid)) {
            resourceCompleteMapper.insert(new ResourceCompleteRecord(rid, uid));
        }
    }

    private void checkStudentCompleteResource(Long rid, Long uid) {
        var resourceRequiredSeconds = videoRequiredSecondsMapper.selectById(rid);
        if (resourceRequiredSeconds == null ||
                resourceRequiredSeconds.getRequiredSeconds() == 0) {
            return;
        }
        // check & remove metadata
        var record = videoWatchedRecordMapper.selectByPrimaryKey(rid, uid);
        Ex.check(record != null && record.getRemainRequiredSeconds() == 0,
                Response.getBadRequest("video-" + rid + "-not-complete")
        );
        videoWatchedRecordMapper.deleteByPrimaryKey(rid, uid);
    }

    @Transactional
    public void completeChapter(Long cid, AuthInfo auth) {
        // check if all video resource complete and add complete
        var uid = auth.getUserID();
        var videoResource = getVideoResources(cid);
        for (var r : videoResource) {
            Ex.check(resourceCompleteMapper.contains(r.getResourceId(), uid),
                    Response.getBadRequest("resource-" + r.getResourceId() + "-not-complete"));
        }
        // add complete
        if(!chapterCompleteMapper.contains(cid, uid)){
            chapterCompleteMapper.insert(new ChapterCompleteRecord(cid, uid));
        }
    }

    private List<Resource> getVideoResources(Long cid) {
        var resources = resourceMapper.getAll(cid);
        return resources.stream()
                .filter(r -> r.getResourceType() == Resource.ResourceType.video)
                .toList();
    }

    public void completeCourse(Long csid, AuthInfo auth) {
        // check if all chapter complete and add complete
        var uid = auth.getUserID();
        var isStudent = userMapper.checkIsStudent(uid);
        if(isStudent){return;}


        var chapters = chapterMapper.getAllChaptersByCourseId(csid);
        for (var c : chapters) {
            Ex.check(chapterCompleteMapper.contains(c.getChapterId(), uid),
                    Response.getBadRequest("chapter-" + c.getChapterId() + "-not-complete"));
        }
        // add complete
        if (!courseCompleteMapper.contains(csid, uid)) {
            courseCompleteMapper.insert(new CourseCompleteRecord(csid, uid));
            applicationEventPublisher.publishEvent(new CompleteCourseEvent(this, csid, uid));
        }
    }


    public void clearAllStudentCourseProgress(Long csid, AuthInfo auth) {
        // only delete course complete record, no effect on chapter and resource
        // TODO AUTH CHECK ONLY TEACHER CAN DO THIS
        courseCompleteMapper.deleteAllByCourseId(csid);
    }

    public void clearAllStudentChapterProgress(Long cid, AuthInfo auth) {
        // no effect on resource and course
        // TODO AUTH CHECK ONLY TEACHER CAN DO THIS
        chapterCompleteMapper.deleteAllByChapterId(cid);
    }

    public void clearAllStudentResourceProgress(Long rid, AuthInfo auth) {
        // no effect on chapter and course
        // TODO AUTH CHECK ONLY TEACHER CAN DO THIS
        resourceCompleteMapper.deleteAllByResourceId(rid);
    }

    @Transactional
    public CourseProgressController.CourseProgress queryCourseProgress(Long csid, AuthInfo auth) {
        var uid = auth.getUserID();
        // for each chapter for each v rs
        var chapters = chapterMapper.getAllChaptersByCourseId(csid);
        chapters = chapters.stream()
                .filter(c -> c.getChapterType() == Chapter.ChapterType.teaching)
                .toList();
        List<CourseProgressController.ChapterProgress> chapterProgresses = new ArrayList<>(chapters.size());
        for (var c : chapters) {
            var cid = c.getChapterId();
            var videoResources = getVideoResources(cid);
            List<CourseProgressController.ResourceProgress> resourceProgresses = new ArrayList<>(videoResources.size());
            for (var r : videoResources) {
                var rid = r.getResourceId();
                var complete = resourceCompleteMapper.contains(rid, uid);
                resourceProgresses.add(new CourseProgressController.ResourceProgress(rid, complete));
            }
            var complete = chapterCompleteMapper.contains(cid, uid);
            chapterProgresses.add(
                    new CourseProgressController.ChapterProgress(
                            cid, resourceProgresses, complete));
        }
        var complete = courseCompleteMapper.contains(csid, uid);
        return new CourseProgressController.CourseProgress(csid, chapterProgresses, complete);
    }
}
