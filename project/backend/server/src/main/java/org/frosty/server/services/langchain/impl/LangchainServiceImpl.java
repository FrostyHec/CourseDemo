package org.frosty.server.services.langchain.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.entity.bo.langchain.ChatHistory;
import org.frosty.server.mapper.langchain.LangchainMapper;
import org.frosty.server.services.langchain.LangchainService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LangchainServiceImpl implements LangchainService {
    private final LangchainMapper langchainMapper;

    @Override
    public void saveChatHistory(LangchainController.ChatContext context, Long id) {
        ChatHistory chatHistory = langchainMapper.selectById(id);


//        // 将 ChatContext 转换为 JsonNode
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonContext = objectMapper.valueToTree(context);

        // 设置 ChatHistory 的 context 字段
        chatHistory.setContext(context);

        langchainMapper.updateById(chatHistory);
    }

    @Override
    public LangchainController.ChatContext getChatContentById(Long id) {

        ChatHistory chatHistory= langchainMapper.selectChatContentByChatId(id);

        return chatHistory.getContext();
    }

    @Override
    public void setChatTitle(String title, Long id) {
        langchainMapper.setChatTitle(title,id);
    }

    @Override
    public LangchainController.ChatEntity getChatEntityById(Long id) {
        return langchainMapper.selectChatEntityByChatId(id);
    }

    @Override
    public List<LangchainController.ChatEntity> getAllMyChatMetadata(long userID) {
        return langchainMapper.selectChatListByUserId(userID);
    }

    @Override
    public void createNewChat(ChatHistory chatHistory) {
        langchainMapper.insert(chatHistory);
    }
}
