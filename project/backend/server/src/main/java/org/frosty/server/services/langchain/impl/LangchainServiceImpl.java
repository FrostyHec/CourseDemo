package org.frosty.server.services.langchain.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.langchain.LangchainController;
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
        langchainMapper.updateByChatId(id,context);
    }

    @Override
    public List<LangchainController.SingleChatMessage> getChatContentById(Long id) {
        return langchainMapper.selectChatContentByChatId(id);
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
}
