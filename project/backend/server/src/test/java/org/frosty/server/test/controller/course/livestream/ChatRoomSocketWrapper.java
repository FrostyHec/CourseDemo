package org.frosty.server.test.controller.course.livestream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frosty.server.controller.course.livestream.ChatRoomWebSocket;
import org.frosty.server.test.tools.WebSocketWrapper;
import org.frosty.test_common.utils.JsonUtils;


public class ChatRoomSocketWrapper {
    private final WebSocketWrapper webSocketWrapper = new WebSocketWrapper();
    private static final String uri ="ws://localhost:9977/websocket/livestream/";
    public static ChatRoomSocketWrapper create(String roomId,Long userId) throws Exception {
        var socket = new ChatRoomSocketWrapper();
        socket.connect(roomId,userId);
        return socket;
    }
    public void connect(String roomId,Long userId) throws Exception {
        webSocketWrapper.connect(uri+"/"+roomId+"/"+userId);
    }

    public void sendMessage(ChatRoomWebSocket.ReceivedChatRoomMessage message) throws Exception{
        webSocketWrapper.sendMessage(
                JsonUtils.toString(message));
    }

    public ChatRoomWebSocket.SendChatRoomMessage receivedMessage() throws Exception{
        var resp  = webSocketWrapper.receiveMessage();
        return JsonUtils.fromString(resp, ChatRoomWebSocket.SendChatRoomMessage.class);
    }

    public ChatRoomWebSocket.SendChatRoomMessage receivedMessage(int timeoutMs) throws Exception{
        var resp  = webSocketWrapper.receiveMessage(timeoutMs);
        return JsonUtils.fromString(resp, ChatRoomWebSocket.SendChatRoomMessage.class);
    }

    public void close() throws Exception {
        webSocketWrapper.close();
    }

    public boolean isOpen() {
        return webSocketWrapper.isOpen();
    }

}

