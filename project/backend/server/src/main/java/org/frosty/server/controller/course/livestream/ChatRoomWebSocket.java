package org.frosty.server.controller.course.livestream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.services.course.livestream.ChatRoomService;
import org.frosty.server.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

@Slf4j
@Component
@ServerEndpoint(PathConstant.API+"/websocket/livestream/{roomId}/{userId}")
public class ChatRoomWebSocket {
    private ChatRoomService chatRoomService;
    private ObjectMapper objectMapper;
    private Session session;
    public void beanInit(){
        chatRoomService = SpringUtils.getBean(ChatRoomService.class);
        objectMapper = SpringUtils.getBean(ObjectMapper.class);
    }
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "roomId") String roomId, @PathParam(value = "userId") Long userId) {
        beanInit();
        this.session = session;
        chatRoomService.addNewSocket(roomId, userId, this);
    }

    @OnClose
    public void onClose() {
        chatRoomService.remove(this);

    }

    @OnMessage
    public void onMessage(String message) {
        try {
            ReceivedChatRoomMessage receivedChatRoomMessage = objectMapper.readValue(message, ReceivedChatRoomMessage.class);
            chatRoomService.send(this, receivedChatRoomMessage);
        } catch (Exception e) {
            log.error("error on message", e);
        }
    }

    public void sendMessage(SendChatRoomMessage sendChatMessage) {
        try {
            session.getAsyncRemote().sendText(objectMapper.writeValueAsString(sendChatMessage));
        } catch (JsonProcessingException e) {
            throw new InternalException("error on sending message", e);
        }
    }

    public interface ChatRoomMessage {
        Long toAll = -1L;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReceivedChatRoomMessage {
        private Long target; // -1 if to all
        private String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SendChatRoomMessage {
        private Long target;
        private UserPublicInfo fromUser;
        private String content;
    }
}
