package org.frosty.server.entity.converter;

import org.frosty.server.controller.course.livestream.ChatRoomWebSocket;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.entity.bo.langchain.ChatHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatHistoryEntityConverter {
    LangchainController.ChatEntity toChatEntity(ChatHistory entity);
}
