package org.frosty.server.entity.converter;

import org.frosty.server.controller.course.livestream.ChatRoomWebSocket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageConverter {
    ChatRoomWebSocket.SendChatRoomMessage toSendChatRoomMessage(ChatRoomWebSocket.ReceivedChatRoomMessage message);
}
