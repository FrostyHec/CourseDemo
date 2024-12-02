package org.frosty.server.services.course.cheat_check;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.cheat_check.VideoWatchRecord;
import org.frosty.server.mapper.course.cheat_check.VideoRequiredSecondsMapper;
import org.frosty.server.mapper.course.cheat_check.VideoWatchedRecordMapper;
import org.frosty.server.mapper.course.progress.CourseCompleteMapper;
import org.frosty.server.mapper.course.progress.ResourceCompleteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheatCheckTransactionalService {
    private final VideoRequiredSecondsMapper videoRequiredSecondsMapper;
    private final VideoWatchedRecordMapper videoWatchedRecordMapper;
    private final ResourceCompleteMapper resourceCompleteMapper;

    @Transactional
    public VideoWatchRecord getWatchedRecord(Long rid, Long uid) {
        if(resourceCompleteMapper.contains(rid,uid)){
            return new VideoWatchRecord(rid,uid,0,0);
        }
        var record = videoWatchedRecordMapper.selectByPrimaryKey(rid, uid);
        // 如果课程的requiredTime不为0且
        var requirement = videoRequiredSecondsMapper.selectById(rid);
        if (record == null) {// 第一次看没记录但有要求
            if (requirement != null &&
                            requirement.getRequiredSeconds() != 0) {//initial time
                record = new VideoWatchRecord(rid, uid, requirement.getRequiredSeconds(), 0);
                videoWatchedRecordMapper.insert(record);
            } else {
                throw new ExternalException(Response.getBadRequest("no-watch-requirement"));
            }
        }
        return record;
    }
}
