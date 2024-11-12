package org.frosty.server.services.langchain;

import org.frosty.server.controller.langchain.LangchainController;

import java.util.List;

public interface LangchainService {
    void saveChatHistory(LangchainController.ChatContext context, Long id);

    List<LangchainController.SingleChatMessage> getChatContentById(Long id);

    void setChatTitle(String title, Long id);

    LangchainController.ChatEntity getChatEntityById(Long id);

    List<LangchainController.ChatEntity> getAllMyChatMetadata(long userID);
}
