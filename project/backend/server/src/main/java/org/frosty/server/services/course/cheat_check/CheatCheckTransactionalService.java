package org.frosty.server.services.course.cheat_check;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.server.entity.bo.cheat_check.VideoWatchRecord;
import org.frosty.server.mapper.course.cheat_check.VideoRequiredSecondsMapper;
import org.frosty.server.mapper.course.cheat_check.VideoWatchedRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheatCheckTransactionalService {
    private final VideoRequiredSecondsMapper videoRequiredSecondsMapper;
    private final VideoWatchedRecordMapper videoWatchedRecordMapper;

    @Transactional
    public VideoWatchRecord getWatchedRecord(Long rid, Long uid) {
        var record = videoWatchedRecordMapper.selectByPrimaryKey(rid, uid);
        // 如果课程的requiredTime不为0且
        var requirement = videoRequiredSecondsMapper.selectById(rid);
        if (record == null && // 第一次看没记录但有要求
                requirement != null &&
                requirement.getRequiredSeconds() != 0) {//initial time
            record = new VideoWatchRecord(rid, uid, requirement.getRequiredSeconds(), 0);
            videoWatchedRecordMapper.insert(record);
        }
        return record;
    }
}
