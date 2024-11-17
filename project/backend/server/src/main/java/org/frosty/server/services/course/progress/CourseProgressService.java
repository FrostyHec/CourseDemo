package org.frosty.server.services.course.progress;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.Resource;
import org.frosty.server.entity.bo.progress.ChapterCompleteRecord;
import org.frosty.server.entity.bo.progress.CourseCompleteRecord;
import org.frosty.server.entity.bo.progress.ResourceCompleteRecord;
import org.frosty.server.mapper.course.ChapterMapper;
import org.frosty.server.mapper.course.ResourceMapper;
import org.frosty.server.mapper.course.cheat_check.VideoRequiredSecondsMapper;
import org.frosty.server.mapper.course.cheat_check.VideoWatchedRecordMapper;
import org.frosty.server.mapper.course.progress.ChapterCompleteMapper;
import org.frosty.server.mapper.course.progress.CourseCompleteMapper;
import org.frosty.server.mapper.course.progress.ResourceCompleteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseProgressService {
    private final ResourceCompleteMapper resourceCompleteMapper;
    private final ChapterCompleteMapper chapterCompleteMapper;
    private final CourseCompleteMapper courseCompleteMapper;
    // resource check mappers
    private final ResourceMapper resourceMapper;
    private final ChapterMapper chapterMapper;
    private final VideoRequiredSecondsMapper videoRequiredSecondsMapper;
    private final VideoWatchedRecordMapper videoWatchedRecordMapper;

    @Transactional
    public void completeResource(Long rid, AuthInfo auth) {
        // if has min required play time check if complete&remove metadata
        // if no, simply complete
        // only complete video resource
        var uid = auth.getUserID();
        var resource = resourceMapper.selectById(rid);
        Ex.check(resource.getResourceType() == Resource.ResourceType.video,
                Response.getBadRequest("cannot-complete-nonvideo"));
        checkStudentCompleteResource(rid, uid);
        // add complete
        resourceCompleteMapper.insert(new ResourceCompleteRecord(rid, uid));
    }
    private void checkStudentCompleteResource(Long rid, Long uid) {
        var resourceRequiredSeconds = videoRequiredSecondsMapper.selectById(rid);
        if (resourceRequiredSeconds == null ||
                resourceRequiredSeconds.getRequiredSeconds() == 0) {
            return;
        }
        // check & remove metadata
        var record = videoWatchedRecordMapper.selectByPrimaryKey(rid,uid);
        Ex.check(record==null||record.getRemainRequiredSeconds()!=0,
                Response.getBadRequest("video-not-complete")
        );
        videoWatchedRecordMapper.deleteByPrimaryKey(rid,uid);
    }

    @Transactional
    public void completeChapter(Long cid, AuthInfo auth) {
        // check if all video resource complete and add complete
        var uid = auth.getUserID();
        var resources = resourceMapper.getAll(cid);
        var videoResource = resources.stream()
                .filter(r -> r.getResourceType() == Resource.ResourceType.video)
                .toList();
        for(var r:videoResource){
            Ex.check(resourceCompleteMapper.contains(r.getResourceId(),uid),
                    Response.getBadRequest("resource-not-complete"));
        }
        // add complete
        chapterCompleteMapper.insert(new ChapterCompleteRecord(cid, uid));
    }

    public void completeCourse(Long csid, AuthInfo auth) {
        // check if all chapter complete and add complete
        var uid = auth.getUserID();
        var chapters = chapterMapper.getAllChaptersByCourseId(csid);
        for(var c:chapters){
            Ex.check(chapterCompleteMapper.contains(c.getChapterId(),uid),
                    Response.getBadRequest("chapter-not-complete"));
        }
        // add complete
        courseCompleteMapper.insert(new CourseCompleteRecord(csid, uid));
    }


    public void clearAllStudentCourseProgress(Long csid, AuthInfo auth) {

    }
}
