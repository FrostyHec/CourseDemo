package org.frosty.site_dispatch.service;

import org.apache.commons.lang3.StringUtils;
import org.frosty.site_dispatch.mapper.SSEIPMapper;
import org.frosty.site_dispatch.mapper.UnackedMapper;
import org.frosty.site_dispatch.mapper.UnposedMapper;
import org.springframework.stereotype.Service;
import org.frosty.site_dispatch.entity.SingleMessageDTO;
import org.frosty.site_dispatch.outerapi.SitePush;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageDispatchService {
    private final SSEIPMapper sseipMapper;
    private final UnposedMapper unposedMapper;
    private final UnackedMapper unackedMapper;
    private final SitePush sitePush;
    @Transactional
    public long push(SingleMessageDTO msg) {
        String ip = sseipMapper.findSSEIP(msg);
        if(StringUtils.isBlank(ip)){
            //没有这个sse连接
            switch (msg.getType()){
                case NEW -> {
                    msg.setMessageId(null);
                    unposedMapper.insert(msg);
                }
                case UPDATE ->{
                    //如果在unposed则更新,如果在unacked则删除unacked,放置入unposed
                    unposedMapper.insertOrUpdate(msg);
                    unackedMapper.deleteIfExists(msg);
                }
                case DELETE -> {
                    //如果在unposed则删除,如果在unacked则删除unacked
                    unposedMapper.deleteById(msg);
                    unackedMapper.deleteIfExists(msg);
                }
            }
            return msg.getMessageId();
        }else {
            switch (msg.getType()) {
                case UPDATE ->{
                    //如果在unposed则更新,如果在unacked则删除unacked,放置入unposed
                    unackedMapper.deleteIfExists(msg);
                }
                case DELETE -> {
                    //如果在unposed则删除,如果在unacked则删除unacked
                    unackedMapper.deleteIfExists(msg);
                }
            }
            //调用这个ip把消息发给pusher
            return sitePush.pushMessage(ip, msg);
        }
    }

    public void ack(long mid) {
        unackedMapper.deleteById(mid);
    }
}
