package org.frosty.server.mapper.course.cheat_check;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.server.entity.bo.cheat_check.VideoWatchRecord;

@Mapper
public interface VideoWatchedRecordMapper extends BaseMapper<VideoWatchRecord> {
    @Select("SELECT * FROM video_watch_records WHERE video_id = #{rid} AND student_id = #{uid}")
    VideoWatchRecord selectByPrimaryKey(Long rid, Long uid);

    @Update("<script>" +
            "UPDATE video_watch_records " +
            "SET remain_required_seconds = #{remainRequiredSeconds}" +
            "<if test='lastWatchedSeconds != null'>" +
            ", last_watched_seconds = #{lastWatchedSeconds}" +
            "</if>" +
            " WHERE video_id = #{videoId} AND student_id = #{studentId}" +
            "</script>")
    void updateByPrimaryKey(VideoWatchRecord record);

    @Delete("DELETE FROM video_watch_records WHERE video_id = #{rid} AND student_id = #{uid}")
    void deleteByPrimaryKey(Long rid, Long uid);
}
