package org.frosty.server.services.course.cheat_check;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.server.controller.course.cheat_check.CheatCheckController;
import org.frosty.server.entity.bo.cheat_check.VideoRequiredSeconds;
import org.frosty.server.entity.bo.cheat_check.VideoWatchRecord;
import org.frosty.server.mapper.course.cheat_check.VideoRequiredSecondsMapper;
import org.frosty.server.mapper.course.cheat_check.VideoWatchedRecordMapper;
import org.frosty.sse.constant.MessageBodyType;
import org.frosty.sse.entity.SiteMessage;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheatCheckService {
    private final VideoRequiredSecondsMapper videoRequiredSecondsMapper;
    private final VideoWatchedRecordMapper videoWatchedRecordMapper;
    private final CheatCheckTransactionalService cheatCheckTransactionalService;
    private final MessagePushService messagePushService;
    private final ObjectMapper objectMapper;
    private final Map<Long, WatchingEntity> userEstablishedWatchingRecord = new ConcurrentHashMap<>();
    // let testing can modify by reflection(instead of being a constant)
    private static final int RECORD_ALIVE_SECONDS = Integer.valueOf(5 * 60);
    private static final int HEART_BEAT_INTERVAL_SECONDS = Integer.valueOf(60);
    private static final double EACH_BEAT_DOWN_TOLERANT = 0.8;

    public void setMinRequiredTime(VideoRequiredSeconds videoRequiredSeconds) {
        videoRequiredSecondsMapper.insert(videoRequiredSeconds);
    }

    public VideoRequiredSeconds getMinRequiredTime(Long videoId) {
        return videoRequiredSecondsMapper.selectById(videoId);
    }

    public VideoWatchRecord getWatchedRecord(Long rid, AuthInfo auth) {
        return cheatCheckTransactionalService.getWatchedRecord(rid, auth.getUserID());
    }

    public void startWatchAlive(Long rid, AuthInfo auth) {
        //开始观看某个视频
        //如果之前存在某个视频则保存那个记录后重新开启一个新的记录
        if (!videoRequiredSecondsMapper.hasWatchRequirement(rid)) {
            throw new ExternalException(Response.getBadRequest("no-watch-requirement"));
        }
        Long uid = auth.getUserID();
        WatchingEntity we = new WatchingEntity(rid, OffsetDateTime.now());
        var previous = userEstablishedWatchingRecord.get(uid);
        if (previous != null) {
            saveWatchingEntityWithoutUser(uid, previous);
        }
        userEstablishedWatchingRecord.put(uid, we);
        we.startTimer(this, uid, RECORD_ALIVE_SECONDS);
        messagePushService.pushSite(SiteMessage.getSimpleSystemNewMessage(uid,
                MessageBodyType.new_video_playing,
                objectMapper.valueToTree(new NewVideoPlayingMessage(rid))
                ));
    }

    public void keepWatchAlive(Long rid, AuthInfo auth) {
        //如果之前没有start必须返回错误
        Long uid = auth.getUserID();
        var we = userEstablishedWatchingRecord.get(uid);
        Ex.check(we != null, new ExternalException(Response.getBadRequest("not-start")));
        we.stopTimer();
        Ex.check(Objects.equals(we.getRid(), rid), new ExternalException(Response.getBadRequest("not-start")));

        //记录心跳
        Ex.check(isHeartBeatValid(we), new ExternalException(Response.getBadRequest("heart-beat-abnormal")));
        we.updateHeartBeat();
        we.startTimer(this, uid, RECORD_ALIVE_SECONDS);
    }

    public void stopWatchAlive(Long rid, AuthInfo auth, CheatCheckController.WatchedInfoEntity watchedInfoEntity) {
        //如果之前没有start必须返回错误
        Long uid = auth.getUserID();
        var we = userEstablishedWatchingRecord.get(uid);
        Ex.check(we != null, new ExternalException(Response.getBadRequest("not-start")));
        we.stopTimer();
        Ex.check(Objects.equals(we.rid, rid), new ExternalException(Response.getBadRequest("not-start")));
        if (!isStopValid(we, watchedInfoEntity)) {
//            saveWatchingEntityWithoutUser(uid, we);
            throw new ExternalException(Response.getBadRequest("stop-abnormal"));
        }
        log.info("stopWatchAlive: uid={}, rid={}, watchedInfoEntity={}", uid, rid, watchedInfoEntity);
        // normal save
        var watchedSeconds = watchedInfoEntity.getWatchedSeconds();
        var watchedRecord = cheatCheckTransactionalService.getWatchedRecord(rid, uid);
        int remainSeconds = watchedRecord.getRemainRequiredSeconds() - watchedSeconds;
        if (remainSeconds < 0) remainSeconds = 0;
        var record = new VideoWatchRecord(rid, uid, remainSeconds, watchedInfoEntity.getWatchedUntil());
        videoWatchedRecordMapper.updateByPrimaryKey(record);
        userEstablishedWatchingRecord.remove(uid);
    }


    private void saveWatchingEntityWithoutUser(Long uid, WatchingEntity we) {
        // 初始时心跳数为1
        Long rid = we.getRid();
        int watchedSeconds = getMinWatchedSeconds(we);
        var watchedRecord = cheatCheckTransactionalService.getWatchedRecord(rid, uid);
        int remainSeconds = watchedRecord.getRemainRequiredSeconds() - watchedSeconds;
        if (remainSeconds < 0) remainSeconds = 0;
        var record = new VideoWatchRecord(rid, uid, remainSeconds, watchedRecord.getLastWatchedSeconds());
        videoWatchedRecordMapper.updateByPrimaryKey(record);
        userEstablishedWatchingRecord.remove(uid);
    }

    private int getMinWatchedSeconds(WatchingEntity we) {
        int cnt = we.getHeartBeatCount() - 1;
        return cnt * HEART_BEAT_INTERVAL_SECONDS;
    }

    private boolean isHeartBeatValid(WatchingEntity we) {
        // 检查1. 当前时间-开始时间 >=已观看seconds
        // 检查2. 当前时间-上次时间 >= 80% * Interval
        int watchedSeconds = getMinWatchedSeconds(we);
        long secondsSinceStartHeartBeat = Duration.between(we.getFirstHeartBeatTime(), OffsetDateTime.now()).getSeconds();
        if (secondsSinceStartHeartBeat < watchedSeconds) {
            return false;
        }
        long secondsSinceLastHeartBeat = Duration.between(we.getLastHeartBeatTime(), OffsetDateTime.now()).getSeconds();
        return secondsSinceLastHeartBeat >= (EACH_BEAT_DOWN_TOLERANT * HEART_BEAT_INTERVAL_SECONDS);
    }

    private boolean isStopValid(WatchingEntity we, CheatCheckController.WatchedInfoEntity watchedInfo) {
        // 检查1. 当前时间-开始时间 >=已观看seconds
        // 检查2. (cnt-1+1)*TimeInterval >= seconds
        int watchedSeconds = watchedInfo.getWatchedSeconds();
        long secondsSinceStartHeartBeat = Duration.between(we.getFirstHeartBeatTime(), OffsetDateTime.now()).getSeconds();
        if (secondsSinceStartHeartBeat + 1 < watchedSeconds) {
            log.warn("secondsSinceStartHeartBeat is " + secondsSinceStartHeartBeat + "but watchedSeconds is " + watchedSeconds);
            return false;
        }
        int maxWatchedSeconds = getMinWatchedSeconds(we) + HEART_BEAT_INTERVAL_SECONDS;
        return maxWatchedSeconds >= watchedSeconds;
    }

    @Data
    public static class WatchingEntity {
        private Long rid;
        private Integer heartBeatCount;
        private OffsetDateTime firstHeartBeatTime;
        private OffsetDateTime lastHeartBeatTime;
        private Timer timer;

        public WatchingEntity(Long rid, OffsetDateTime firstHeartBeatTime) {
            this.rid = rid;
            this.heartBeatCount = 1;
            this.firstHeartBeatTime = firstHeartBeatTime;
            this.lastHeartBeatTime = firstHeartBeatTime;
        }

        public void startTimer(CheatCheckService service, Long uid, int seconds) {
            if (timer != null) {
                timer.cancel(); // 取消现有的计时器
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    service.saveWatchingEntityWithoutUser(uid, WatchingEntity.this);
                }
            }, (long) seconds * 1000); // 设置倒计时（分钟转换为毫秒）
        }

        public void resetTimer(CheatCheckService service, Long uid, int minutes) {
            startTimer(service, uid, minutes); // 重置计时器
        }

        public void stopTimer() {
            if (timer != null) {
                timer.cancel(); // 取消计时器
            }
        }

        public void updateHeartBeat() {
            heartBeatCount++;
            lastHeartBeatTime = OffsetDateTime.now();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewVideoPlayingMessage {
        Long resource_id;
    }
}
