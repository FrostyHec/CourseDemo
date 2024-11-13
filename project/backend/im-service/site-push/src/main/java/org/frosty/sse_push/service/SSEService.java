package org.frosty.sse_push.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.frosty.sse.converter.SiteMessageConverter;
import org.frosty.sse.entity.SSEIPEntity;
import org.frosty.sse.entity.SiteMessage;
import org.frosty.sse.entity.SiteMessagePacketDTO;
import org.frosty.sse.entity.SitePushDTO;
import org.frosty.sse_push.config.RunArgs;
import org.frosty.sse_push.mapper.SSEIPMapper;
import org.frosty.sse_push.mapper.UnackedMapper;
import org.frosty.sse_push.mapper.UnposedMapper;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class SSEService {
    private final SSEIPMapper sseipMapper;
    private final UnposedMapper unposedMapper;
    private final UnackedMapper unackedMapper;
    private final RunArgs runArgs;
    private final SiteMessageConverter converter;
    private final Map<Long, List<SseEmitter>> clients = new ConcurrentHashMap<>();

    public void push(SiteMessage dto) {//push的msg都是unposed的
        long to = dto.getToId(), msgId = dto.getMessageId();
//        if (dto.getMessageId() == null) {//set msg id
//            msgId = IdWorker.getId();
//            dto.setMessageId(msgId);
//        } else {
//            msgId = dto.getMessageId();
//        }
        // get all emitters
        var emitters = clients.get(to);
        if (emitters == null || emitters.isEmpty()) {
            log.warn("no connection established on uid:" + to);
            unposedMapper.insertOrUpdate(converter.toUnposed(dto));
            return;
        }
        // push message to all emitters
        int successCount = 0;
        for (int i = emitters.size() - 1; i >= 0; i--) {
            var emitter = emitters.get(i);
            try {
                sendMsg(emitter, new SitePushDTO(dto));
                successCount++;
            } catch (IOException e) {
                log.info("cannot push message to user:" + to);
//                log.error("error on pushing message to user:" + to,e);
                sseClosed(to, emitter);
            }
        }
        // if no any success, restore the message to unposed
        if (successCount == 0) {
            log.info("No message pushed successfully to user:" + to);
            log.warn("storing message to unposed:" + dto);
            unposedMapper.insertOrUpdate(converter.toUnposed(dto));
            return;
        }
        // accept if at least one success
        log.info("Total "+successCount+" message pushed successfully to user:" + to);
        if (dto.isRequiredAck()) {
            unackedMapper.insert(converter.toUnacked(dto));
        }
    }

    private void sendMsg(SseEmitter emitter, SitePushDTO dto) throws IOException {
        emitter.send(Response.getSuccess(dto), MediaType.APPLICATION_JSON);
    }


    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000))
    @Transactional
    public SseEmitter register(long uid) {
        sseipMapper.insertIfNotExist(new SSEIPEntity(uid, getSelfIp()));//假连接优于丢失连接管理。先记录再创建
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> {
            log.info("emitter complete:" + uid);
            sseClosed(uid, emitter);
        });
        emitter.onTimeout(() -> {
            log.warn("emitter timeout:" + uid);
            sseClosed(uid, emitter);
        });
        addEmitter(uid, emitter);
        try {
            initialSend(emitter, uid);
        } catch (Exception e) {
            throw new InternalException("unknown-internal-error", e);
        }
        log.info("connection established on uid:" + uid);
        return emitter;
    }

    public void addEmitter(long uid, SseEmitter emitter) {
        List<SseEmitter> emitters = clients.getOrDefault(uid, new CopyOnWriteArrayList<>());
        emitters.add(emitter);
        clients.put(uid, emitters);
    }

    private void initialSend(SseEmitter emitter, long uid) throws IOException {
        //TODO 为避免事务问题, 五秒钟后再拉取一次
        var unposeds = unposedMapper.selectByToId(uid);
        var unackeds = unackedMapper.selectByToId(uid);
        var dto = new SiteMessagePacketDTO(unposeds, unackeds);
        sendMsg(emitter, new SitePushDTO(dto));
        var requiredAckList = unposeds.stream()
                .filter(SiteMessage::isRequiredAck)
                .map(converter::toUnacked)
                .toList();
        unackedMapper.insert(requiredAckList);
        if (!unposeds.isEmpty()) {
            unposedMapper.deleteByIds(unposeds);
        }
    }

    private void sseClosed(long uid, SseEmitter emitter) {
        clients.get(uid).remove(emitter);
        if (clients.get(uid).isEmpty()) {
            sseipMapper.delete(uid);
        }
    }

    private String getSelfIp() {
        if (runArgs.onK8s) {
            return System.getenv("POD_IP");
        } else {
            try {
                return InetAddress.getLocalHost().getHostAddress() + ":" + runArgs.port;
            } catch (UnknownHostException e) {
                throw new InternalException("unknown-internal-error", e);
            }
        }
    }

//    private long pushError(long to, SingleMessageDTO dto, Exception e) {
//        log.error("error on pushing message to user:" + to, e);
//        log.warn("storing message to unposed:" + dto);
//        sseClosed(to);
//        unposedMapper.insertOrUpdate(dto);
//        return dto.getMessageId();
//    }
}
