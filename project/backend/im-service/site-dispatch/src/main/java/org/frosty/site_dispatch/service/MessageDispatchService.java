package org.frosty.site_dispatch.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import org.frosty.site_dispatch.mapper.SSEIPMapper;
import org.frosty.site_dispatch.mapper.UnackedMapper;
import org.frosty.site_dispatch.mapper.UnposedMapper;
import org.frosty.sse.converter.SiteMessageConverter;
import org.frosty.sse.entity.SiteMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageDispatchService {
    private final SSEIPMapper sseipMapper;
    private final UnposedMapper unposedMapper;
    private final UnackedMapper unackedMapper;
    private final SiteMessageConverter converter;
    private final SitePush sitePush;

    @Transactional
    public long push(SiteMessage msg) {
        var ips = sseipMapper.findSSEIP(msg);
        if (ips==null||ips.isEmpty()) {
            //没有这个sse连接
            switch (msg.getType()) {
                case NEW -> {
                    msg.setMessageId(IdWorker.getId());
                    unposedMapper.insert(converter.toUnposed(msg));
                }
                case UPDATE -> {
                    //如果在unposed则更新,如果在unacked则删除unacked,放置入unposed
                    unposedMapper.insertOrUpdate(converter.toUnposed(msg));
                    unackedMapper.deleteIfExists(msg);
                }
                case DELETE -> {
                    //如果在unposed则删除,如果在unacked则删除unacked
                    unposedMapper.deleteById(msg.getMessageId());
                    unackedMapper.deleteIfExists(msg);
                }
            }
        } else {
            // 存在sse连接
            for(var ip:ips) {
                switch (msg.getType()) {
                    case NEW -> {
                        msg.setMessageId(IdWorker.getId());
                    }
                    case UPDATE -> {
                        //如果在unposed则更新,如果在unacked则删除unacked,放置入unposed
                        unackedMapper.deleteIfExists(msg);
                    }
                    case DELETE -> {
                        //如果在unposed则删除,如果在unacked则删除unacked
                        unackedMapper.deleteIfExists(msg);
                    }
                }
                //调用这个ip把消息发给pusher
                sitePush.pushMessage(ip, msg);
            }
        }
        return msg.getMessageId();
    }

    public void ack(long mid) {
        unackedMapper.deleteById(mid);
    }
}
