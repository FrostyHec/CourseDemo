package org.frosty.server.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.frosty.server.entity.bo.FileSubmission;

public interface FileSubmissionMapper extends BaseMapper<FileSubmission> {
    FileSubmission selectPreviousSubmission(Long assignmentId, Long studentId);
}
