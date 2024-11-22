package org.frosty.server.services.course.livestream;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.frosty.server.controller.course.livestream.ChatRoomWebSocket;
import org.frosty.server.controller.course.livestream.ChatRoomWebSocket.ChatRoomMessage;
import org.frosty.server.controller.course.livestream.ChatRoomWebSocket.ReceivedChatRoomMessage;
import org.frosty.server.entity.converter.ChatMessageConverter;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatMessageConverter converter;
    private final UserMapper userMapper;
    private final Map<ChatRoomWebSocket, Pair<String,Long>> webSockets = new HashMap<>();
    private final Map<String, Map<Long,ChatRoomWebSocket>> socketPool = new HashMap<>();

    public void addNewSocket(String roomId, Long userId, ChatRoomWebSocket chatRoomWebSocket) {
        Map<Long, ChatRoomWebSocket> room = socketPool.computeIfAbsent(roomId, k -> new HashMap<>());
        room.put(userId, chatRoomWebSocket);
        webSockets.put(chatRoomWebSocket, Pair.of(roomId, userId));
        log.info("new connection on "+roomId+" by user: "+userId);
    }

    public void remove(ChatRoomWebSocket chatRoomWebSocket) {
        Pair<String, Long> pair = webSockets.get(chatRoomWebSocket);
        socketPool.get(pair.getLeft()).remove(pair.getRight());
        var roomId = pair.getLeft();
        var userId = pair.getRight();
        socketPool.get(roomId).remove(userId);
        if (socketPool.get(roomId).isEmpty()) {
            socketPool.remove(roomId);
        }
        webSockets.remove(chatRoomWebSocket);
        log.info("connection closed on "+roomId+" by user: "+userId);
    }


    public void send(ChatRoomWebSocket chatRoomWebSocket, ReceivedChatRoomMessage receivedChatMessage) {
        Pair<String, Long> pair = webSockets.get(chatRoomWebSocket);
        var roomId = pair.getLeft();
        var senderId = pair.getRight();

        var roomMap = socketPool.get(roomId);

        var sendChatMessage = converter.toSendChatRoomMessage(receivedChatMessage);
        sendChatMessage.setFromUser(userMapper.findPublicInfoById(senderId));

        if(receivedChatMessage.getTarget() == ChatRoomMessage.toAll){ // to all
            for (var eachEntry: roomMap.entrySet()){
                eachEntry.getValue().sendMessage(sendChatMessage);
            }
            return;
        }
        // to single
        var receiverId = receivedChatMessage.getTarget();
        var receiverSocket = roomMap.get(receiverId);
        receiverSocket.sendMessage(sendChatMessage);
    }
}
